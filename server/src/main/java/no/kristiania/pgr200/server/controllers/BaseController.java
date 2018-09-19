package no.kristiania.pgr200.server.controllers;

import no.kristiania.pgr200.common.Http.HttpMethod;
import no.kristiania.pgr200.server.HeaderResponse;

import java.io.PrintWriter;

public class BaseController extends HeaderResponse {

    private PrintWriter out;
    private String id;
    private String parameters;
    private String route;
    private HttpMethod method;

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
