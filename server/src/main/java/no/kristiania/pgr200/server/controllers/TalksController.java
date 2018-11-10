package no.kristiania.pgr200.server.controllers;

import no.kristiania.pgr200.common.http.HttpMethod;
import no.kristiania.pgr200.common.http.HttpRequest;
import no.kristiania.pgr200.common.http.HttpResponse;
import no.kristiania.pgr200.server.annotations.ApiController;
import no.kristiania.pgr200.server.annotations.ApiRequest;
import no.kristiania.pgr200.server.models.TalkModel;

@ApiController("/talks")
public class TalksController extends BaseController<TalkModel> {

    public TalksController(HttpRequest httpRequest) {
        super(httpRequest);
    }

    @Override
    @ApiRequest(action = HttpMethod.GET, route = "/talks")
    public HttpResponse index() {
        return index(new TalkModel().newQuery().with("topic").get());
    }

    @Override
    @ApiRequest(action = HttpMethod.GET, route = "/talks" + uuidPath)
    public HttpResponse show() {
        if (!validateUUID(getHttpRequest().getUri().split("/")[2])) return getNotValidUuidResponse();
        TalkModel model = new TalkModel();
        return show(model.newQuery()
                         .whereEquals(model.getPrimaryKey(), getUuidFromUri()).with("topic").first());
    }

    @Override
    @ApiRequest(action = HttpMethod.POST, route = "/talks")
    public HttpResponse create() {
        return create(new TalkModel(getHttpRequest().getJson().getAsJsonObject()));
    }

    @Override
    @ApiRequest(action = HttpMethod.PUT, route = "/talks" + uuidPath)
    public HttpResponse update() {
        if (!validateUUID(getHttpRequest().getUri().split("/")[2])) return getNotValidUuidResponse();
        return update(new TalkModel(getUuidFromUri(),
                                    getHttpRequest().getJson().getAsJsonObject()));
    }

    @Override
    @ApiRequest(action = HttpMethod.DELETE, route = "/talks" + uuidPath)
    public HttpResponse destroy() {
        if (!validateUUID(getHttpRequest().getUri().split("/")[2])) return getNotValidUuidResponse();
        return destroy(new TalkModel(getUuidFromUri()));
    }
}
