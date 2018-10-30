package no.kristiania.pgr200.server.controllers;

import no.kristiania.pgr200.common.http.HttpMethod;
import no.kristiania.pgr200.common.http.HttpRequest;
import no.kristiania.pgr200.common.http.HttpResponse;
import no.kristiania.pgr200.server.annotations.ApiController;
import no.kristiania.pgr200.server.annotations.ApiRequest;
import no.kristiania.pgr200.server.models.TalkModel;
import no.kristiania.pgr200.server.models.TopicModel;

@ApiController("/topics")
public class TopicsController extends BaseController<TopicModel>{

    public TopicsController(HttpRequest httpRequest){
        super(httpRequest);
    }

    @Override
    @ApiRequest(action = HttpMethod.GET, route = "/topics")
    public HttpResponse index() {
        return index(new TopicModel().newQuery().get());
    }

    @Override
    @ApiRequest(action = HttpMethod.GET, route = "/topics" + uuidPath)
    public HttpResponse show() {
        return show(new TopicModel().newQuery().first());
    }

    @Override
    @ApiRequest(action = HttpMethod.PUT, route = "/topics" +uuidPath)
    public HttpResponse update() {
        return update(new TopicModel(getUuidFromUri(), getHttpRequest().getJson().getAsJsonObject()));
    }

    @Override
    @ApiRequest(action = HttpMethod.POST, route = "/topics")
    public HttpResponse create() {
        return create(new TopicModel(getHttpRequest().getJson().getAsJsonObject()));
    }

    @Override
    @ApiRequest(action = HttpMethod.DELETE, route = "/topics" + uuidPath)
    public HttpResponse destroy() {
        return destroy(new TopicModel(getUuidFromUri()));
    }
}
