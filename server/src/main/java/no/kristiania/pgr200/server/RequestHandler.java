package no.kristiania.pgr200.server;

import no.kristiania.pgr200.common.http.HttpRequest;
import no.kristiania.pgr200.common.http.HttpResponse;
import no.kristiania.pgr200.common.http.HttpStatus;
import no.kristiania.pgr200.server.annotations.ApiController;
import no.kristiania.pgr200.server.annotations.ApiRequest;
import no.kristiania.pgr200.server.controllers.*;

import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

class RequestHandler {

    private HttpRequest httpRequest;
    private BaseController controller;

    RequestHandler(HttpRequest httpRequest) {
        this.httpRequest = httpRequest;
    }

    void processRequest(OutputStream out) throws InvocationTargetException, IllegalAccessException, IOException {
        setController(getControllerFromRoute());
        Method controllerMethod = null;
        if (controller != null) controllerMethod = getMethodFromAnnotation(controller);
        if (controllerMethod != null) {
            HttpResponse httpResponse = (HttpResponse) controllerMethod.invoke(controller);
            httpResponse.writeToStream(out);
        } else {
            new HttpResponse(HttpStatus.NotFound).writeToStream(out);
        }
    }

    BaseController getControllerFromRoute() {
        for (Map.Entry<BaseController, ApiController> entry : getControllers().entrySet()) {
            Pattern regex = Pattern.compile(entry.getValue().value());
            Matcher matcher = regex.matcher(httpRequest.getUri());
            if (matcher.find()) return entry.getKey();
        }
        return null;
    }

    private HashMap<BaseController, ApiController> getControllers() {
        HashMap<BaseController, ApiController> annotations = new HashMap<>();
        annotations.put(new TalksController(httpRequest), TalksController.class.getAnnotation(ApiController.class));
        annotations.put(new TopicsController(httpRequest), TopicsController.class.getAnnotation(ApiController.class));
        annotations.put(new DaysController(httpRequest), DaysController.class.getAnnotation(ApiController.class));
        annotations.put(new ConferencesController(httpRequest),
                        ConferencesController.class.getAnnotation(ApiController.class));
        annotations.put(new TracksController(httpRequest), TracksController.class.getAnnotation(ApiController.class));
        annotations.put(new TimeslotsController(httpRequest),
                        TimeslotsController.class.getAnnotation(ApiController.class));
        return annotations;
    }

    Method getMethodFromAnnotation(BaseController controller) {
        Method[] methods = controller.getClass().getDeclaredMethods();
        for (Method method : methods) {
            if (method.isAnnotationPresent(ApiRequest.class)) {
                ApiRequest annotation = method.getAnnotation(ApiRequest.class);
                if (httpRequest.getUri().split("/").length == annotation.route().split("/").length
                        && annotation.action().equals(httpRequest.getHttpMethod())
                        && Pattern.compile(annotation.route()).matcher(httpRequest.getUri()).find()) {
                    return method;
                }
            }
        }
        return null;
    }

    public BaseController getController() {
        return controller;
    }

    public void setController(BaseController controller) {
        this.controller = controller;
    }
}
