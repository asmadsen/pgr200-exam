package no.kristiania.pgr200.server.controllers;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import no.kristiania.pgr200.common.Http.HttpMethod;
import no.kristiania.pgr200.common.Http.HttpRequest;
import no.kristiania.pgr200.common.Http.HttpResponse;
import no.kristiania.pgr200.server.annotations.ApiRequest;

import java.lang.annotation.Inherited;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public abstract class BaseController {

    public static final String uuidPath = "/\\S+";
    public static final String uuid = "[0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}";

    private final HttpRequest httpRequest;
    private final JsonObject body;
    private final Gson gsonBuilder;
    protected HttpResponse httpResponse;

    public BaseController(HttpRequest httpRequest) {
        this.httpRequest = httpRequest;
        this.httpResponse = new HttpResponse();
        this.body = new JsonObject();
        this.gsonBuilder = new GsonBuilder().setPrettyPrinting().create();
    }

    public Map<String, String> getHeaders(String body){
        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");
        headers.put("Connection", "close");
        headers.put("Content-Length", String.valueOf(body.length()));
        headers.put("Date: ", new Date().toString());
        headers.put("Server", "Conference API server");
        return headers;
    }

    @ApiRequest(action = HttpMethod.GET, route = "/")
    abstract HttpResponse index();

    @ApiRequest(action = HttpMethod.GET, route = uuidPath)
    abstract HttpResponse show();

    @ApiRequest(action = HttpMethod.PUT, route = uuidPath)
    abstract HttpResponse update();

    @ApiRequest(action = HttpMethod.POST, route = "/")
    abstract HttpResponse create();

    @ApiRequest(action = HttpMethod.DELETE, route = uuidPath)
    abstract HttpResponse destroy();

    public JsonObject getBody() {
        return body;
    }

    public Gson getGsonBuilder() {
        return gsonBuilder;
    }

    public HttpRequest getHttpRequest() {
        return httpRequest;
    }

    public HttpResponse getHttpResponse() {
        return httpResponse;
    }

    boolean validateUUID(String uuid){
        System.out.println(uuid);
        System.out.println(BaseController.uuid);
        return uuid.matches(BaseController.uuid);
    }

    JsonObject getErrorMessage(String message){
        JsonObject errorObject = new JsonObject();
        errorObject.addProperty("message", message);
        return errorObject;
    }
}
