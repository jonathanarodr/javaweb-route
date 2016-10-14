package br.com.route;

import br.com.route.annotation.*;
import br.com.route.type.ResponseType;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Set;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.reflections.Reflections;

@WebServlet(name = "route", urlPatterns = {"/home","/param/*"}) //acesso ao servidor tomcat
public class Route extends HttpServlet {

    private final String packageRoute = "br.com.route.resource";
    
    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        try {            
            boolean isValidURI = false;

            //captura classes com anotação @Controller
            Reflections reflections = new Reflections(this.packageRoute);
            Set<Class<?>> controllers = reflections.getTypesAnnotatedWith(Controller.class);
            String requestURI = request.getRequestURI();
            
            //percorre classes e localiza mapeamento de rota
            for (Class<?> controller : controllers) {
                if (this.find(controller.getAnnotation(Controller.class).value(), requestURI)) {
                    
                    isValidURI = true;
                    
                    //captura configurações da controller
                    ResponseType responseType = controller.getAnnotation(Response.class).value();
                    String requestMapping = controller.getAnnotation(RequestMapping.class).value();                    
                    
                    //cria instância e executa requisição
                    Class<?> classref = Class.forName(controller.getName());
                    IRoute route = (IRoute) classref.newInstance();
                    route.execute(request, response);
                    
                    //configura response
                    switch (responseType) {
                        case DISPATCHER : request.getRequestDispatcher(requestMapping).forward(request, response);
                            break;
                        case REDIRECT : response.sendRedirect(requestMapping);
                            break;
                        case JSON :
                            break;
                    }
                    
                    break;
                }
            }
            
            if (!isValidURI) {
                response.sendError(404, "Route " + requestURI + " not found");
                return;
            }
        } catch(Exception ex) {
            throw new ServletException(ex);
        }
    }
    
    private boolean find(String annotationURI, String requestURI) {
        boolean valid = false;
        String[] annotationSplit = annotationURI.split("/");
        String[] requestSplit = requestURI.split("/");
        
        if (annotationSplit.length != requestSplit.length) {
            return false;
        }
        
        for (int i=0; i<requestSplit.length; i++) {            
            if ((!annotationSplit[i].equals(requestSplit[i])) && (!annotationSplit[i].equals("*"))) {
                valid = false;
                break;
            }
            
            valid = true;
        }
        
        return valid;
    }
    
    public static String getParamURIDecode(String uri) {
        try {
            String[] values = uri.split("/");
            return URLDecoder.decode(values[values.length-1], "UTF-8");
        } catch (UnsupportedEncodingException ex) {
            return null;
        }
    }
    
    @Deprecated
    public static String getParamURI(String uri) {
        String[] values = uri.split("/");
        return values[values.length-1];
    }
    
}