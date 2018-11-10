package no.kristiania.pgr200.server.controllers;

import no.kristiania.pgr200.common.http.HttpMethod;
import no.kristiania.pgr200.common.http.HttpRequest;
import no.kristiania.pgr200.common.http.HttpResponse;
import no.kristiania.pgr200.server.annotations.ApiController;
import no.kristiania.pgr200.server.annotations.ApiRequest;
import no.kristiania.pgr200.server.models.ConferenceModel;

@ApiController("/conferences")
public class ConferencesController extends BaseController<ConferenceModel> {


    public ConferencesController(HttpRequest httpRequest) {
        super(httpRequest);
    }

    @Override
    @ApiRequest(action = HttpMethod.GET, route = "/conferences")
    public HttpResponse index() {
        return index(new ConferenceModel().newQuery().get());
    }

    @Override
    @ApiRequest(action = HttpMethod.GET, route = "/conferences" + uuidPath)
    public HttpResponse show() {
        if (!validateUUID(getHttpRequest().getUri().split("/")[2])) return getNotValidUuidResponse();
        ConferenceModel model = new ConferenceModel();
        return show(model.newQuery()
                         .whereEquals(model.getPrimaryKey(), getUuidFromUri()).with("days").first());
    }

    @Override
    @ApiRequest(action = HttpMethod.PUT, route = "/conferences" + uuidPath)
    public HttpResponse update() {
        if (!validateUUID(getHttpRequest().getUri().split("/")[2])) return getNotValidUuidResponse();
        return update(new ConferenceModel(getUuidFromUri(), getHttpRequest().getJson().getAsJsonObject()));
    }

    @Override
    @ApiRequest(action = HttpMethod.POST, route = "/conferences")
    public HttpResponse create() {
        return create(new ConferenceModel(getHttpRequest().getJson().getAsJsonObject()));
    }

    @Override
    @ApiRequest(action = HttpMethod.DELETE, route = "/conferences" + uuidPath)
    public HttpResponse destroy() {
        if (!validateUUID(getHttpRequest().getUri().split("/")[2])) return getNotValidUuidResponse();
        return destroy(new ConferenceModel(getUuidFromUri()));
    }
}
