package no.kristiania.pgr200.server.controllers;

import no.kristiania.pgr200.common.http.HttpMethod;
import no.kristiania.pgr200.common.http.HttpRequest;
import no.kristiania.pgr200.common.http.HttpResponse;
import no.kristiania.pgr200.server.annotations.ApiController;
import no.kristiania.pgr200.server.annotations.ApiRequest;
import no.kristiania.pgr200.server.models.TopicModel;
import no.kristiania.pgr200.server.models.TrackModel;

@ApiController("/tracks")
public class TracksController extends BaseController<TrackModel> {

    public TracksController(HttpRequest httpRequest) {
        super(httpRequest);
    }

    @Override
    @ApiRequest(action = HttpMethod.GET, route = "/tracks")
    public HttpResponse index() {
        return index(new TrackModel().newQuery().get());
    }

    @Override
    @ApiRequest(action = HttpMethod.GET, route = "/tracks" + uuidPath)
    public HttpResponse show() {
        return show(new TrackModel(getUuidFromUri()));
    }

    @Override
    @ApiRequest(action = HttpMethod.PUT, route = "/tracks" + uuidPath)
    public HttpResponse update() {
        return update(new TrackModel(getUuidFromUri(), getHttpRequest().getJson().getAsJsonObject()));
    }

    @Override
    @ApiRequest(action = HttpMethod.POST, route = "/tracks")
    public HttpResponse create() {
        return create(new TrackModel(getHttpRequest().getJson().getAsJsonObject()));
    }

    @Override
    @ApiRequest(action = HttpMethod.DELETE, route = "/tracks" + uuidPath)
    public HttpResponse destroy() {
        return destroy(new TrackModel(getUuidFromUri()));
    }
}
