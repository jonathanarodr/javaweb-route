package br.com.route;

import br.com.route.annotation.*;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Method;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.reflections.Reflections;
import org.reflections.scanners.MethodAnnotationsScanner;
import org.reflections.scanners.SubTypesScanner;
import org.reflections.scanners.TypeAnnotationsScanner;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;
import org.reflections.util.FilterBuilder;

@WebServlet(name = "route", urlPatterns = {"/home", "/teste/*"}) //acesso ao servidor tomcat
public class Route extends HttpServlet {
    
    public static final FilterBuilder filter = new FilterBuilder().include("br.com.route.resource");
    private Map<String,String> paramMap = new HashMap<String,String>();
    
    @Override
    protected void service(HttpServletRequest httpRequest, HttpServletResponse httpResponse)
            throws ServletException, IOException {

        try {
            //captura classes com anotação @Controller
            Reflections reflections = this.reflectionBuilder();
            Set<Class<?>> controllers = reflections.getTypesAnnotatedWith(Controller.class);
            String requestURI = httpRequest.getRequestURI();
            
            String mainpath = "";
            String path = "";
            boolean isValidURI = false;
            
            System.out.println(requestURI);
            
            //percorre classes e localiza mapeamento de rota
            for (Class<?> controller : controllers) {
                mainpath = controller.getAnnotation(Controller.class).value();
                
                //captura métodos da classe e verifica path
                Set<Method> methods = reflections.getMethodsAnnotatedWith(Path.class);
                
                for (Method method : methods) {
                    path = this.pathBuilder(mainpath, method.getAnnotation(Path.class).value());

                    //valida request
                    if ((this.find(path, requestURI)) && (this.validMethod(method, httpRequest.getMethod()))) {
                        isValidURI = true;

                        //extrai parâmetros da url
                        this.paramMap.entrySet().forEach((param) -> {
                            httpRequest.setAttribute(param.getKey(), param.getValue());
                        });

                        //executa método
                        Object object = controller.newInstance();
                        Response response = (Response) method.invoke(object, httpRequest, httpResponse);
                        
                        if (response == null) {
                            return;
                        }
                        
                        //efetua serialização da entidade caso exista
                        if ((method.isAnnotationPresent(Produces.class)) && (response.entity != null)) {
                            switch (method.getAnnotation(Produces.class).value()) {
                                case APPLICATION_FORM_URLENCODED : httpRequest.setAttribute(response.entity.getClass().getSimpleName(), response.entity);
                                    break;
                                case APPLICATION_JSON :
                                    break;
                                case APPLICATION_XML :
                                    break;
                            }
                        }
                        
                        //se response foi chamado, executa ação
                        switch (response.responseType) {
                            case DISPATCHER : httpRequest.getRequestDispatcher(response.location).forward(httpRequest, httpResponse);
                                break;
                            case REDIRECT : httpResponse.sendRedirect(response.location);
                                break;
                            case ERROR : if (response.entity == null) { httpResponse.sendError(response.status); } else { httpResponse.sendError(response.status, (String) response.entity); };
                                break;
                        }
                        
                        break;
                    }
                }
                
                if (isValidURI) {
                    break;
                }
            }
            
            //se uri não foi encontrada, envia erro 404
            if (!isValidURI) {
                httpResponse.sendError(404, "Route " + requestURI + " not found");
                return;
            }
        } catch(Exception ex) {
            ex.printStackTrace();
            throw new ServletException(ex);
        }
    }
    
    private Reflections reflectionBuilder() {
        return new Reflections(new ConfigurationBuilder().setUrls(ClasspathHelper.forPackage("br.com.route.resource"))
                .setScanners(new SubTypesScanner(false)
                            ,new TypeAnnotationsScanner()
                            ,new MethodAnnotationsScanner()));
    }
    
    private String pathBuilder(String path1, String path2) {
        if (path2.startsWith("/")) {
            return path2;
        }
        
        return (path1.endsWith("/")) ? path1 + path2 : path1 + "/" + path2;
    }
    
    private boolean find(String annotationURI, String requestURI) {
        if (annotationURI.equals(requestURI)) {
            return true;
        }
        
        boolean valid = false;
        String[] annotationSplit = annotationURI.split("/");
        String[] requestSplit = requestURI.split("/");
        
        if (annotationSplit.length != requestSplit.length) {
            return false;
        }
        
        //captura parâmetros da requisição
        String key;
        String value;
        for (int i=0; i<requestSplit.length; i++) {
            key = annotationSplit[i];
            value = requestSplit[i];
            
            if ((key.startsWith("{")) && (key.endsWith("}"))) {
                paramMap.put(key.replace("{", "").replace("}", ""), this.getParamURIDecode(value));
                annotationSplit[i] = "*";
            }
        }
        
        //valida mapeamento da requisição
        for (int i=0; i<requestSplit.length; i++) {
            if ((!annotationSplit[i].equals(requestSplit[i])) && (!annotationSplit[i].equals("*"))) {
                valid = false;
                break;
            }
            
            valid = true;
        }
        
        return valid;
    }
    
    private boolean validMethod(Method method, String requestMethod) {
        return (((requestMethod.equals(GET.class.getSimpleName())) && ((method.isAnnotationPresent(GET.class))))
              ||((requestMethod.equals(POST.class.getSimpleName())) && ((method.isAnnotationPresent(POST.class))))
              ||((requestMethod.equals(PUT.class.getSimpleName())) && ((method.isAnnotationPresent(PUT.class))))
              ||((requestMethod.equals(DELETE.class.getSimpleName())) && ((method.isAnnotationPresent(DELETE.class)))));
    }
    
    private String getParamURIDecode(String paramValue) {
        try {            
            return URLDecoder.decode(paramValue, "UTF-8");
        } catch (UnsupportedEncodingException ex) {
            return null;
        }
    }
    
}