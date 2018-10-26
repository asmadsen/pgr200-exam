package no.kristiania.pgr200.server.controllers;

import no.kristiania.pgr200.common.Http.HttpMethod;
import no.kristiania.pgr200.common.Http.HttpRequest;
import no.kristiania.pgr200.common.Http.HttpResponse;
import no.kristiania.pgr200.common.Http.HttpStatus;
import no.kristiania.pgr200.server.annotations.ApiController;
import no.kristiania.pgr200.server.annotations.ApiRequest;

@ApiController("/topic")
public class TopicController extends BaseController{

    public TopicController(HttpRequest httpRequest){
        super(httpRequest);
    }

    @Override
    @ApiRequest(action = HttpMethod.GET, route = "/")
    HttpResponse index() {
        return new HttpResponse(HttpStatus.NotImplemented);
    }

    @Override
    @ApiRequest(action = HttpMethod.GET, route = uuidPath)
    HttpResponse show() {
        return new HttpResponse(HttpStatus.NotImplemented);
    }

    @Override
    @ApiRequest(action = HttpMethod.PUT, route = uuidPath)
    HttpResponse update() {
        return new HttpResponse(HttpStatus.NotImplemented);
    }

    @Override
    @ApiRequest(action = HttpMethod.POST, route = "/")
    HttpResponse create() {
        return new HttpResponse(HttpStatus.NotImplemented);
    }

    @Override
    @ApiRequest(action = HttpMethod.DELETE, route = uuidPath)
    HttpResponse destroy() {
        return new HttpResponse(HttpStatus.NotImplemented);
    }
}
