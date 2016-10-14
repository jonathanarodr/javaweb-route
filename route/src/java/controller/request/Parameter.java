package controller.request;

import controller.Command;
import controller.Route;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class Parameter implements Command {

    @Override
    public String executeCommand(HttpServletRequest request, HttpServletResponse response) {
        request.setAttribute("id", Route.getParamURIDecode(request.getRequestURI()));
        return null;
    }

}