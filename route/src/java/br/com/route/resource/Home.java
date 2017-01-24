package br.com.route.resource;

import br.com.route.Response;
import br.com.route.Route;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import br.com.route.annotation.Controller;
import br.com.route.annotation.GET;
import br.com.route.annotation.MediaType;
import br.com.route.annotation.Path;
import br.com.route.annotation.Produces;

@Controller("/")
public class Home extends Route {
    
    @GET
    @Path("/index.jsp")
    @Produces(MediaType.APPLICATION_JSON)
    public void index(HttpServletRequest request, HttpServletResponse response) {
        request.setAttribute("teste", "Jonathan");
    }
    
    @GET
    @Path("home")
    @Produces(MediaType.APPLICATION_JSON)
    public Response home(HttpServletRequest request, HttpServletResponse response) {
        System.out.println("Executando controller /home...");
        return new Response().ok("/WEB-INF/home.jsp");
    }
    
    @GET
    @Path("teste/{msg}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response teste(HttpServletRequest request, HttpServletResponse response) {
        request.setAttribute("mensagem", request.getAttribute("msg"));
        System.out.println("Executando controller /teste..."); 
        return new Response().ok("/WEB-INF/teste.jsp");
    }
    
}    
