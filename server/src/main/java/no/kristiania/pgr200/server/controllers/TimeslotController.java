package no.kristiania.pgr200.server.controllers;

import no.kristiania.pgr200.common.http.HttpMethod;
import no.kristiania.pgr200.common.http.HttpRequest;
import no.kristiania.pgr200.common.http.HttpResponse;
import no.kristiania.pgr200.server.annotations.ApiController;
import no.kristiania.pgr200.server.annotations.ApiRequest;
import no.kristiania.pgr200.server.models.TimeslotModel;

@ApiController("/timeslots")
public class TimeslotController extends BaseController<TimeslotModel> {

    public TimeslotController(HttpRequest httpRequest) {
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
        TimeslotModel model = new TimeslotModel();
        return show(model.newQuery()
                .whereEquals(model.getPrimaryKey(), getUuidFromUri()).with("track").with("talk").first());
    }

    @Override
    @ApiRequest(action = HttpMethod.PUT, route = "/timeslots" + uuidPath)
    public HttpResponse update() {
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
        return destroy(new TimeslotModel(getUuidFromUri()));
    }
}
