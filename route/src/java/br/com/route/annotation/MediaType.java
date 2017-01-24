package br.com.route.annotation;

public enum MediaType {
    APPLICATION_XML("application/xml"),
    APPLICATION_JSON("application/json"),
    APPLICATION_FORM_URLENCODED("application/x-www-form-urlencoded");
    
    private String type;
    
    MediaType(String type) {
        this.type = type;
    }
    
    public String value() {
        return this.type;
    }
    
}
