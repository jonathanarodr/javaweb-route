package controller;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;

public class Route {
    
    private boolean status;
    private String urlMapping;
    private String typeResponse;
    private String packageName;
    private final int prUrlRoute     = 0;
    private final int prUrlMapping   = 1;
    private final int prTypeResponse = 2;
    private final int prPackageName  = 3;
    private static final List<String[]> arrMapping = new ArrayList<>();
    
    public boolean isStatus() {
        return status;
    }
    
    public String getUrlMapping() {
        return urlMapping;
    }
    
    public String getTypeResponse() {
        return typeResponse;
    }

    public String getPackageName() {
        return this.packageName;
    }
    
    private void mapping() {
        arrMapping.clear();
        this.status       = false;
        this.urlMapping   = null;
        this.typeResponse = null;
        this.packageName  = null;
        
        //urlRoute deve ser único pois é a chave de busca da rota
        add("/route/index", "/route", "redirect", null);
        add("/route/home", "/WEB-INF/home.jsp", "dispatcher", null);
        add("/route/param/*", "/WEB-INF/param/parameter.jsp", "dispatcher", "controller.request.Parameter");
    }
    
    private void add(String urlRoute, String urlMapping, String typeResponse, String packageName) {
        String mappingAdd[] = {urlRoute, urlMapping, typeResponse, packageName};
        arrMapping.add(mappingAdd);
    }
    
    private boolean isValidURI(String url, String uri) {
        boolean  valid    = false;
        String[] urlSplit = url.split("/");
        String[] uriSplit = uri.split("/");
        
        if (urlSplit.length != uriSplit.length) {
            return false;
        }
        
        for (int i=0; i<uriSplit.length; i++) {            
            if ((!urlSplit[i].equals(uriSplit[i])) && (!urlSplit[i].equals("*"))) {
                valid = false;
                break;
            }
            
            valid = true;
        }
        
        return valid;
    }
    
    public Route find(String uri) {
        mapping();
        
        for (String[] mapping : arrMapping) {
            if (this.isValidURI(mapping[prUrlRoute], uri)) {
                this.status       = true;
                this.urlMapping   = mapping[prUrlMapping];
                this.typeResponse = mapping[prTypeResponse];
                this.packageName  = mapping[prPackageName];
                break;
            }
        }
        
        return this;
    }
    
    public static String getParamURI(String uri) {
        String[] values = uri.split("/");
        return values[values.length-1];
    }
    
    public static String getParamURIDecode(String uri) {
        try {
            String[] values = uri.split("/");
            return URLDecoder.decode(values[values.length-1], "UTF-8");
        } catch (UnsupportedEncodingException ex) {
            return null;
        }
    }
     
}