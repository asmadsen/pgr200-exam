package no.kristiania.pgr200.server;

import no.kristiania.pgr200.common.Http.HttpMethod;

import java.io.PrintWriter;
import java.lang.reflect.Method;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class BaseController extends HeaderResponse implements Routing {

    private PrintWriter out;
    private String id;
    private String parameters;
    private String route;
    private HttpMethod method;

    @Override
    public Method parseRoute() throws Exception {
        throw new Exception("Not implemented!");
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
