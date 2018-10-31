package no.kristiania.pgr200.server.controllers;

import com.google.gson.*;
import no.kristiania.pgr200.common.http.HttpMethod;
import no.kristiania.pgr200.common.http.HttpRequest;
import no.kristiania.pgr200.common.http.HttpResponse;
import no.kristiania.pgr200.common.http.HttpStatus;
import no.kristiania.pgr200.orm.BaseRecord;
import no.kristiania.pgr200.server.annotations.ApiRequest;

import javax.validation.ConstraintViolation;
import java.util.*;

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
        this.gsonBuilder = new GsonBuilder().setPrettyPrinting().serializeNulls().create();
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

    HttpResponse index(List<T> models) {
        JsonArray list = new JsonArray();
        for (T model : models) list.add(model.toJson());
        getResponsebody().add("values", list);
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
            addPropertyToBody("error",
                    getErrorMessage("Could not find element with id: " + getHttpRequest().getUri().split("/")[2]));
            httpResponse.setBody(getResponsebody().toString());
        } else {
            httpResponse.setStatus(HttpStatus.OK);
            if(model != null) {
                addPropertyToBody("value", model.toJson());
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
        if (!validateUUID(getHttpRequest().getUri().split("/")[2])) {
            getResponsebody().add("error",
                    getErrorMessage("Could not find element with id: " + getHttpRequest().getUri().split("/")[2]));
            httpResponse.setStatus(HttpStatus.BadRequest);
            httpResponse.setBody(getResponsebody().toString());
        }
        Set<ConstraintViolation> violations = model.getState().validate();
        httpResponse.setHeaders(getHeaders());
        if (violations.isEmpty() && model.update()) {
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
            return getElementNotFoundResponse();
        }
        if (model.destroy()) {
            httpResponse.setStatus(HttpStatus.OK);
        } else {
            httpResponse.setStatus(HttpStatus.BadRequest);
            JsonObject object = new JsonObject();
            object.add("error", getErrorMessage("Could not delete element with id: " + getUuidFromUri()));
            httpResponse.setBody(object.toString());
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

    HttpResponse getElementNotFoundResponse() {
        getResponsebody().add("error",
                getErrorMessage("Could not find element with id: " + getHttpRequest().getUri().split("/")[2]));
        httpResponse.setStatus(HttpStatus.BadRequest);
        httpResponse.setBody(getResponsebody().toString());
        return httpResponse;
    }
}
