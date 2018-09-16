package no.kristiania.pgr200.server;

import no.kristiania.pgr200.common.Http.HttpRequest;

@ApiController("/schedule")
public class ScheduleController extends BaseController {

    private HttpRequest httpRequest;

    public ScheduleController(HttpRequest httpRequest){
        this.httpRequest = httpRequest;
    }
}
