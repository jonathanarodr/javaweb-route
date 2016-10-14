package br.com.route.resource;

import br.com.route.IRoute;
import br.com.route.annotation.Controller;
import br.com.route.annotation.RequestMapping;
import br.com.route.annotation.Response;
import br.com.route.type.ResponseType;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller("/param/{paramId}")
@RequestMapping("/WEB-INF/param/parameter.jsp")
@Response(ResponseType.DISPATCHER)
public class Param implements IRoute {

    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response, Map<String,String> paramMap) {
        System.out.println(paramMap.get("paramId"));
        request.setAttribute("id", paramMap.get("paramId"));
        System.out.println("Executando controller /param/*...");
    }
    
}