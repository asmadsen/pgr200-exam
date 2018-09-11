package no.kristiania.pgr200.server;

import java.io.PrintWriter;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class BaseController extends HeaderResponse {

    private PrintWriter out;
    private String id;
    private String parameters;

    public Method getMethodFromAnnotation(String request) {
        String[] tokens = request.split(" ");
        final List<Method> controllerMethods = new ArrayList<>(Arrays.asList(TalksController.class.getDeclaredMethods()));
        Method requestedMethod = null;
        for (Method controllerMethod : controllerMethods) {
            ApiRequest annotation = controllerMethod.getAnnotation(ApiRequest.class);
            if (annotation != null) {
                String route = tokens[1].split("/")[2];
                if (route.contains("?")) {
                    setParameters(route.split("[?]")[1]);
                    route = route.split("[?]")[0];
                }
                Pattern pattern = Pattern.compile(annotation.route());
                Matcher matcher = pattern.matcher(route);
                if (tokens[0].matches(annotation.action()) && matcher.find()) {
                    requestedMethod = controllerMethod;
                }
            }
        }
        return requestedMethod;
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
}
