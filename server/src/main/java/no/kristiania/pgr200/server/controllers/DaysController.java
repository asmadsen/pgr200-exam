package no.kristiania.pgr200.server.controllers;

import com.google.gson.JsonObject;
import no.kristiania.pgr200.common.http.HttpMethod;
import no.kristiania.pgr200.common.http.HttpRequest;
import no.kristiania.pgr200.common.http.HttpResponse;
import no.kristiania.pgr200.common.http.HttpStatus;
import no.kristiania.pgr200.server.annotations.ApiController;
import no.kristiania.pgr200.server.annotations.ApiRequest;
import no.kristiania.pgr200.server.models.DayModel;
import org.slf4j.Logger;

import java.time.format.DateTimeParseException;

import static org.slf4j.LoggerFactory.getLogger;

@ApiController("/days")
public class DaysController extends BaseController<DayModel> {
    private static Logger logger = getLogger(DaysController.class);

    public DaysController(HttpRequest httpRequest) {
        super(httpRequest);
    }

    @Override
    @ApiRequest(action = HttpMethod.GET, route = "/days")
    public HttpResponse index() {
        return index(new DayModel().newQuery().get());
    }

    @Override
    @ApiRequest(action = HttpMethod.GET, route = "/days" + uuidPath)
    public HttpResponse show() {
        if (!validateUUID(getHttpRequest().getUri().split("/")[2])) return getNotValidUuidResponse();
        DayModel model = new DayModel();
        return show(model.newQuery()
                         .whereEquals(model.getPrimaryKey(), getUuidFromUri()).with("conference").first());
    }

    @Override
    @ApiRequest(action = HttpMethod.PUT, route = "/days" + uuidPath)
    public HttpResponse update() {
        if (!validateUUID(getHttpRequest().getUri().split("/")[2])) return getNotValidUuidResponse();
        try {
            return update(new DayModel(getUuidFromUri(), getHttpRequest().getJson().getAsJsonObject()));
        } catch (DateTimeParseException e) {
            logger.error("update", e);
            return getDateTimeParseResponse(e.getMessage());
        }
    }

    @Override
    @ApiRequest(action = HttpMethod.POST, route = "/days")
    public HttpResponse create() {
        try {
            return create(new DayModel(getHttpRequest().getJson().getAsJsonObject()));
        } catch (DateTimeParseException e) {
            logger.error("create", e);
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
        if (!validateUUID(getHttpRequest().getUri().split("/")[2])) return getNotValidUuidResponse();
        return destroy(new DayModel(getUuidFromUri()));
    }
}
