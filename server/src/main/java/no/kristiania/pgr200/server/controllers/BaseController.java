package no.kristiania.pgr200.server.controllers;

import no.kristiania.pgr200.common.Http.HttpMethod;

import java.io.PrintWriter;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class BaseController {

    private PrintWriter out;
    private String id;
    private String parameters;
    private String route;
    private HttpMethod method;

    public Map<String, String> getHeaders(String body){
        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");
        headers.put("Connection", "close");
        headers.put("Content-Length", String.valueOf(body.length()));
        headers.put("Date: ", new Date().toString());
        headers.put("Server", "Conference API server");
        return headers;
    }

    public PrintWriter getOut() {
        return out;
    }

    public void setOut(PrintWriter out) {
        this.out = out;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getParameters() {
        return parameters;
    }

    public void setParameters(String parameters) {
        this.parameters = parameters;
    }

    public String getRoute() {
        return route;
    }

    public void setRoute(String route) {
        this.route = route;
    }

    public HttpMethod getMethod() {
        return method;
    }

    public void setMethod(HttpMethod method) {
        this.method = method;
    }
}
