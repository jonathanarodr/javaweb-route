package br.com.route;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface IRoute {
    public void execute(HttpServletRequest request, HttpServletResponse response);    
}
