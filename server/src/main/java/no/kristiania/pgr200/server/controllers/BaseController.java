package no.kristiania.pgr200.server.controllers;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import no.kristiania.pgr200.common.Http.HttpMethod;
import no.kristiania.pgr200.common.Http.HttpRequest;
import no.kristiania.pgr200.common.Http.HttpResponse;
import no.kristiania.pgr200.common.Http.HttpStatus;
import no.kristiania.pgr200.orm.BaseRecord;
import no.kristiania.pgr200.server.annotations.ApiRequest;

import javax.validation.ConstraintViolation;
import java.util.*;
import java.util.stream.Collectors;

public abstract class BaseController<T extends BaseRecord> {

    public static final String uuidPath = "/\\S+";
    public static final String uuid = "[0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}";

    private final HttpRequest httpRequest;
    private final JsonObject responsebody;
    private final Gson gsonBuilder;
    protected HttpResponse httpResponse;

    public BaseController(HttpRequest httpRequest) {
        this.httpRequest = httpRequest;
        this.httpResponse = new HttpResponse();
        this.responsebody = new JsonObject();
        this.gsonBuilder = new GsonBuilder().setPrettyPrinting().create();
    }

    public Map<String, String> getHeaders() {
        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");
        headers.put("Connection", "close");
        headers.put("Date: ", new Date().toString());
        headers.put("Server", "Conference API server");
        return headers;
    }

    @ApiRequest(action = HttpMethod.GET, route = "/")
    public abstract HttpResponse index();

    HttpResponse index(T model) {
        List<T> list = model.all();
        getResponsebody().add("values", new JsonParser().parse(getGsonBuilder().toJson(
                list.stream().map(BaseRecord::getState).collect(Collectors.toList()))));
        httpResponse.setHeaders(getHeaders());
        httpResponse.setBody(getGsonBuilder().toJson(getResponsebody()));
        httpResponse.setStatus(HttpStatus.OK);
        return httpResponse;
    }

    @ApiRequest(action = HttpMethod.GET, route = uuidPath)
    public abstract HttpResponse show();

    HttpResponse show(T model) {
        httpResponse.setHeaders(getHeaders());
        if (!validateUUID(getHttpRequest().getUri().split("/")[2])){
            httpResponse.setStatus(HttpStatus.BadRequest);
            getResponsebody().add("error",
                    getErrorMessage("Could not find element with id: " + getHttpRequest().getUri().split("/")[2]));
            httpResponse.setBody(getResponsebody().toString());
        } else {
            T result = (T) model.findById(getUuidFromUri());
            httpResponse.setStatus(HttpStatus.OK);
            if(result != null) {
                addPropertyToBody("value",
                        new JsonParser().parse(getGsonBuilder().toJson(result.getState())).getAsJsonObject());
            } else {
                JsonObject object = new JsonObject();
                object.addProperty("data", "No results");
                addPropertyToBody("value", object);
            }
            httpResponse.setBody(getGsonBuilder().toJson(getResponsebody()));
        }
        return httpResponse;
    }

    @ApiRequest(action = HttpMethod.PUT, route = uuidPath)
    public abstract HttpResponse update();

    HttpResponse update(T model) {
        Set<ConstraintViolation> violations = model.getState().validate();
        httpResponse.setHeaders(getHeaders());
        if (violations.isEmpty() && model.save()) {
            getResponsebody().add("value", new JsonParser().parse(getGsonBuilder().toJson(model.getState())));
            httpResponse.setStatus(HttpStatus.OK);
            httpResponse.setBody(getResponsebody().toString());
        } else {
            httpResponse.setStatus(HttpStatus.UnprocessableEntity);
            if (getHttpRequest().getJson().getAsJsonObject().get("error") != null) {
                httpResponse.setBody(getGsonBuilder().toJson(httpRequest.getJson()));
            } else {
                addViolations(violations);
                httpResponse.setBody(getGsonBuilder().toJson(getResponsebody()));
            }
        }
        return httpResponse;
    }

    @ApiRequest(action = HttpMethod.POST, route = "/")
    public abstract HttpResponse create();

    HttpResponse create(T model) {
        Set<ConstraintViolation> violations = model.getState().validate();
        httpResponse.setHeaders(getHeaders());
        if (violations.isEmpty() && model.create()) {
            getResponsebody().add("value", new JsonParser().parse(getGsonBuilder().toJson(model.getState())));
            httpResponse.setStatus(HttpStatus.Created);
            httpResponse.setBody(getGsonBuilder().toJson(getResponsebody()));
        } else {
            httpResponse.setStatus(HttpStatus.UnprocessableEntity);
            if (getHttpRequest().getJson().getAsJsonObject().get("error") != null) {
                httpResponse.setBody(getGsonBuilder().toJson(httpRequest.getJson()));
            } else {
                addViolations(violations);
                httpResponse.setBody(getGsonBuilder().toJson(getResponsebody()));
            }
        }
        return httpResponse;
    }

    @ApiRequest(action = HttpMethod.DELETE, route = uuidPath)
    public abstract HttpResponse destroy();

    HttpResponse destroy(T model) {
        httpResponse.setHeaders(getHeaders());
        if (!validateUUID(getHttpRequest().getUri().split("/")[2])) {
            getResponsebody().add("error",
                    getErrorMessage("Could not find element with id: " + getHttpRequest().getUri().split("/")[2]));
            httpResponse.setStatus(HttpStatus.BadRequest);
            httpResponse.setBody(getResponsebody().toString());
        }
        if (model.destroy()) {
            httpResponse.setStatus(HttpStatus.OK);
        } else {
            httpResponse.setStatus(HttpStatus.BadRequest);
        }
        return httpResponse;
    }

    public JsonObject getResponsebody() {
        return responsebody;
    }

    public Gson getGsonBuilder() {
        return gsonBuilder;
    }

    public HttpRequest getHttpRequest() {
        return httpRequest;
    }

    UUID getUuidFromUri() {
        return UUID.fromString(getHttpRequest().getUri().split("/")[2]);
    }

    boolean validateUUID(String uuid) {
        return uuid.matches(BaseController.uuid);
    }

    JsonObject getErrorMessage(String message) {
        JsonObject errorObject = new JsonObject();
        errorObject.addProperty("message", message);
        return errorObject;
    }

    void addPropertyToBody(String property, JsonObject value) {
        getResponsebody().add(property, value);
    }

    private void addViolations(Set<ConstraintViolation> violations) {
        JsonObject errors = new JsonObject();
        for (ConstraintViolation violation : violations) {
            errors.addProperty(violation.getPropertyPath().toString(), violation.getMessage());
        }
        getResponsebody().add("errors", errors);
    }
}
