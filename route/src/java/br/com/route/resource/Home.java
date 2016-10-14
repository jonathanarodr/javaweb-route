package br.com.route.resource;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import br.com.route.IRoute;
import br.com.route.annotation.Controller;
import br.com.route.annotation.RequestMapping;
import br.com.route.type.ResponseType;
import br.com.route.annotation.Response;

@Controller("/home")
@RequestMapping("/WEB-INF/home.jsp")
@Response(ResponseType.DISPATCHER)
public class Home implements IRoute {

    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) {
        System.out.println("Executando controller /home...");
    }
    
}