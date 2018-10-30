package no.kristiania.pgr200.server.controllers;

import no.kristiania.pgr200.common.http.HttpMethod;
import no.kristiania.pgr200.common.http.HttpRequest;
import no.kristiania.pgr200.common.http.HttpResponse;
import no.kristiania.pgr200.common.http.HttpStatus;
import no.kristiania.pgr200.server.annotations.ApiController;
import no.kristiania.pgr200.server.annotations.ApiRequest;

@ApiController("/schedule")
public class ScheduleController extends BaseController {

    public ScheduleController(HttpRequest httpRequest){
        super(httpRequest);
    }

    @Override
    @ApiRequest(action = HttpMethod.GET, route = "/schedule")
    public HttpResponse index() {
        return new HttpResponse(HttpStatus.NotImplemented);
    }

    @Override
    @ApiRequest(action = HttpMethod.GET, route = "/schedule" + uuidPath)
    public HttpResponse show() {
        return new HttpResponse(HttpStatus.NotImplemented);
    }

    @Override
    @ApiRequest(action = HttpMethod.PUT, route = "/schedule" + uuidPath)
    public HttpResponse update() {
        return new HttpResponse(HttpStatus.NotImplemented);
    }

    @Override
    @ApiRequest(action = HttpMethod.POST, route = "/schedule")
    public HttpResponse create() {
        return new HttpResponse(HttpStatus.NotImplemented);
    }

    @Override
    @ApiRequest(action = HttpMethod.DELETE, route = "/schedule" + uuidPath)
    public HttpResponse destroy() {
        return new HttpResponse(HttpStatus.NotImplemented);
    }
}
