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

    public Method getMethodFromAnnotation(String request) {
        String[] tokens = request.split(" ");
        setRoute(tokens[1]);
        final Method[] requestedMethod = {null};
        getControllers().forEach((controllerClass,apiAnnotation) -> {
            Pattern regex = Pattern.compile(apiAnnotation.value());
            Matcher matcher = regex.matcher(getRoute());
            if(matcher.find()){
                try {
                    requestedMethod[0] = controllerClass.parseRoute(HttpMethod.valueOf(tokens[0]), getRoute());
                } catch (Exception e) {
                    // TODO: Respond with HttpCode

                }
            }
        });

//        if (route.contains("?")) {
//            setParameters(route.split("[?]")[1]);
//            route = route.split("[?]")[0];
//        }
//        String controllerName = route.split("/")[2];
//        final List<Method> controllerMethods = new ArrayList<>(
//                Arrays.asList(Class.forName(getClass().getPackage().getName() + "." +
//                        Helpers.capitalize(controllerName) + "Controller").getDeclaredMethods()));

//        for (Method controllerMethod : controllerMethods) {
//            ApiRequest annotation = controllerMethod.getAnnotation(ApiRequest.class);
//            if (annotation != null) {
//                Pattern pattern = Pattern.compile(annotation.route());
//                Matcher matcher = pattern.matcher(route);
//                System.out.println(tokens[0].matches(annotation.action().name()) && matcher.find());
//                if (tokens[0].matches(annotation.action().name()) && matcher.find()) {
//                    setRoute(route);
//                    requestedMethod = controllerMethod;
//                }
//            }
//        }
        return requestedMethod[0];
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

    private HashMap<BaseController, ApiController> getControllers(){
        // TODO: Use enum for controller classes?
        HashMap<BaseController, ApiController> annotations = new HashMap<>();
        annotations.put(new TalksController(), TalksController.class.getAnnotation(ApiController.class));
        annotations.put(new ScheduleController(), ScheduleController.class.getAnnotation(ApiController.class));
        return annotations;
    }

    @Override
    public Method parseRoute(HttpMethod method, String route) throws Exception {
        throw new Exception("Not implemented!");
    }
}
