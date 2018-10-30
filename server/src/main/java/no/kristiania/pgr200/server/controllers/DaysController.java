package no.kristiania.pgr200.server.controllers;

import com.google.gson.JsonObject;
import no.kristiania.pgr200.common.http.HttpMethod;
import no.kristiania.pgr200.common.http.HttpRequest;
import no.kristiania.pgr200.common.http.HttpResponse;
import no.kristiania.pgr200.common.http.HttpStatus;
import no.kristiania.pgr200.server.annotations.ApiController;
import no.kristiania.pgr200.server.annotations.ApiRequest;
import no.kristiania.pgr200.server.models.DayModel;

import java.time.format.DateTimeParseException;

@ApiController("/days")
public class DaysController extends BaseController<DayModel> {

    public DaysController(HttpRequest httpRequest) {
        super(httpRequest);
    }

    @Override
    @ApiRequest(action = HttpMethod.GET, route = "/days")
    public HttpResponse index() {
        return index(new DayModel());
    }

    @Override
    @ApiRequest(action = HttpMethod.GET, route = "/days" + uuidPath)
    public HttpResponse show() {
        return show(new DayModel());
    }

    @Override
    @ApiRequest(action = HttpMethod.PUT, route = "/days" + uuidPath)
    public HttpResponse update() {
        try {
            return update(new DayModel(getUuidFromUri(), getHttpRequest().getJson().getAsJsonObject()));
        } catch (DateTimeParseException e) {
            return getDateTimeParseResponse(e.getMessage());
        }
    }

    @Override
    @ApiRequest(action = HttpMethod.POST, route = "/days")
    public HttpResponse create() {
        try {
            return create(new DayModel(getHttpRequest().getJson().getAsJsonObject()));
        } catch (DateTimeParseException e) {
            return getDateTimeParseResponse(e.getMessage());
        }
    }

    private HttpResponse getDateTimeParseResponse(String message) {
        JsonObject error = new JsonObject();
        error.addProperty("message", message);
        addPropertyToBody("error", error);
        httpResponse.setStatus(HttpStatus.UnprocessableEntity);
        httpResponse.setBody(getResponsebody().toString());
        httpResponse.setHeaders(getHeaders());
        return httpResponse;
    }

    @Override
    @ApiRequest(action = HttpMethod.DELETE, route = "/days" + uuidPath)
    public HttpResponse destroy() {
        return destroy(new DayModel(getUuidFromUri()));
    }
}
