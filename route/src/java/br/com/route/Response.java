package br.com.route;

import br.com.route.type.ResponseType;

public class Response {
    
    protected int status;
    protected String location;
    protected Object entity;
    protected ResponseType responseType;
    
    public Response ok(String location) {
        this.status = 200;
        this.location = location;
        this.entity = null;
        this.responseType = ResponseType.DISPATCHER;
        return this;
    }
    
    public Response ok(String location, Object entity) {
        this.status = 200;
        this.location = location;
        this.entity = entity;
        this.responseType = ResponseType.DISPATCHER;
        return this;
    }
    
    public Response created(String location) {
        this.status = 201;
        this.location = location;
        this.entity = null;
        this.responseType = ResponseType.REDIRECT;
        return this;
    }
    
    public Response created(String location, Object entity) {
        this.status = 201;
        this.location = location;
        this.entity = entity;
        this.responseType = ResponseType.REDIRECT;
        return this;
    }
    
    public Response redirect(String location) {
        this.status = 302;
        this.location = location;
        this.entity = null;
        this.responseType = ResponseType.REDIRECT;
        return this;
    }
    
    public Response redirect(String location, Object entity) {
        this.status = 302;
        this.location = location;
        this.entity = entity;
        this.responseType = ResponseType.REDIRECT;
        return this;
    }
    
    public Response error(int status) {
        this.status = status;
        this.entity = null;
        this.responseType = ResponseType.ERROR;
        return this;
    }
    
    public Response error(int status, String message) {
        this.status = status;
        this.entity = message;
        this.responseType = ResponseType.ERROR;
        return this;
    }
    
}
