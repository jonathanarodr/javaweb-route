package controller;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@MultipartConfig
@WebServlet(name = "controller", urlPatterns = {"/index","/home","/param/*"}) //acesso ao servidor tomcat
public class Controller extends HttpServlet {
    
    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        try {
            Route  objRoute   = new Route();
            String requestURI = request.getRequestURI();
            
            //se mapeamento de rota não foi localizado gera erro 500
            if (!objRoute.find(requestURI).isStatus()) {
                response.sendError(500, "Route " + requestURI + " not found");
                return;
            }
            
            //se existe pacote, cria sua instância e faz execução
            if (objRoute.getPackageName() != null) {
                Class<?> classe = Class.forName(objRoute.getPackageName());
                Command  comand = (Command) classe.newInstance();
                
                //executa chamada de pacote
                requestURI = (comand.executeCommand(request, response));
                
                //se houver novo redirecionamento, localiza mapeamento de rota
                if (requestURI != null) {
                    objRoute.find(requestURI);
                }
            }
            
            //gera resposta de requisição do cliente
            switch (objRoute.getTypeResponse()) {
                case "dispatcher" : request.getRequestDispatcher(objRoute.getUrlMapping()).forward(request, response);
                    break;
                case "redirect" : response.sendRedirect(objRoute.getUrlMapping());
                    break;
                default : throw new IllegalArgumentException("Type response " + objRoute.getTypeResponse() + " not found");                
            }
        } catch(Exception ex) {
            throw new ServletException(ex);
        }
    }

}