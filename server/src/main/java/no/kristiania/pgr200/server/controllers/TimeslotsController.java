package no.kristiania.pgr200.server.controllers;

import no.kristiania.pgr200.common.http.HttpMethod;
import no.kristiania.pgr200.common.http.HttpRequest;
import no.kristiania.pgr200.common.http.HttpResponse;
import no.kristiania.pgr200.server.annotations.ApiController;
import no.kristiania.pgr200.server.annotations.ApiRequest;
import no.kristiania.pgr200.server.models.TimeslotModel;

@ApiController("/timeslots")
public class TimeslotsController extends BaseController<TimeslotModel> {

    public TimeslotsController(HttpRequest httpRequest) {
        super(httpRequest);
    }

    @Override
    @ApiRequest(action = HttpMethod.GET, route = "/timeslots")
    public HttpResponse index() {
        return index(new TimeslotModel().newQuery().get());
    }

    @Override
    @ApiRequest(action = HttpMethod.GET, route = "/timeslots" + uuidPath)
    public HttpResponse show() {
        if (!validateUUID(getHttpRequest().getUri().split("/")[2])) return getNotValidUuidResponse();
        TimeslotModel model = new TimeslotModel();
        return show(model.newQuery()
                         .whereEquals(model.getPrimaryKey(), getUuidFromUri()).with("track").with("talk").first());
    }

    @Override
    @ApiRequest(action = HttpMethod.PUT, route = "/timeslots" + uuidPath)
    public HttpResponse update() {
        if (!validateUUID(getHttpRequest().getUri().split("/")[2])) return getNotValidUuidResponse();
        return update(new TimeslotModel(getUuidFromUri(), getHttpRequest().getJson().getAsJsonObject()));
    }

    @Override
    @ApiRequest(action = HttpMethod.POST, route = "/timeslots")
    public HttpResponse create() {
        return create(new TimeslotModel(getHttpRequest().getJson().getAsJsonObject()));
    }

    @Override
    @ApiRequest(action = HttpMethod.DELETE, route = "/timeslots" + uuidPath)
    public HttpResponse destroy() {
        if (!validateUUID(getHttpRequest().getUri().split("/")[2])) return getNotValidUuidResponse();
        return destroy(new TimeslotModel(getUuidFromUri()));
    }
}
