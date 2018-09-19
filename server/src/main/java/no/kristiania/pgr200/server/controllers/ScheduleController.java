package no.kristiania.pgr200.server.controllers;

import no.kristiania.pgr200.common.Http.HttpRequest;
import no.kristiania.pgr200.server.controllers.ApiController;
import no.kristiania.pgr200.server.controllers.BaseController;

@ApiController("/schedule")
public class ScheduleController extends BaseController {

    private HttpRequest httpRequest;

    public ScheduleController(HttpRequest httpRequest){
        this.httpRequest = httpRequest;
    }
}
