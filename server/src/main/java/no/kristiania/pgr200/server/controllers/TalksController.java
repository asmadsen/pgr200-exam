package no.kristiania.pgr200.server.controllers;


import com.google.gson.*;
import no.kristiania.pgr200.common.Http.HttpMethod;
import no.kristiania.pgr200.common.Http.HttpRequest;
import no.kristiania.pgr200.common.Http.HttpResponse;
import no.kristiania.pgr200.common.Http.HttpStatus;
import no.kristiania.pgr200.server.annotations.ApiController;
import no.kristiania.pgr200.server.annotations.ApiRequest;
import no.kristiania.pgr200.server.models.TalkModel;

import java.sql.SQLException;

@ApiController("/talks")
public class TalksController extends BaseController {

    private HttpRequest httpRequest;
    private HttpResponse httpResponse;
    private TalkModel talk;
    private Gson gson;
    private JsonObject body;

    public TalksController(HttpRequest httpRequest){
        this.httpRequest = httpRequest;
        this.httpResponse = new HttpResponse();
        this.talk = new TalkModel();
        this.body = new JsonObject();
        this.gson = new GsonBuilder().setPrettyPrinting().create();
    }

    @ApiRequest(action = HttpMethod.GET, route = "/api/talks")
    public HttpResponse index() throws Exception {
        body.add("values", new JsonParser().parse(gson.toJson(TalkModel.all())));
        httpResponse.setHeaders(getHeaders(gson.toJson(body)));
        httpResponse.setBody(gson.toJson(body));
        httpResponse.setStatus(HttpStatus.OK);
        return httpResponse;
    }

    @ApiRequest(action = HttpMethod.GET, route = "/api/talks/\\d+")
    public HttpResponse show() throws Exception {
        body.add("value", new JsonParser().parse(gson.toJson(
                TalkModel.findBy(Integer.parseInt(httpRequest.getUri().split("/")[3])))));
        httpResponse.setHeaders(getHeaders(gson.toJson(body)));
        httpResponse.setBody(gson.toJson(body));
        httpResponse.setStatus(HttpStatus.OK);
        return httpResponse;
    }

    @ApiRequest(action = HttpMethod.POST, route = "/api/talks")
    public HttpResponse create( ) throws SQLException {
        System.out.println("TEST POST Create");
        JsonElement jsonElement = new TalkModel(httpRequest.getJson()).create();
        if(jsonElement == null) return new HttpResponse(HttpStatus.UnprocessableEntity);
        return new HttpResponse(HttpStatus.Created, jsonElement);
    }

    @ApiRequest(action = HttpMethod.PATCH, route = "/api/talks/\\d+")
    public HttpResponse update(){
        System.out.println("TEST PATCH Update");
        return null;
    }

    @ApiRequest(action = HttpMethod.DELETE, route = "/api/talks/\\d+")
    public HttpResponse destroy(){
        System.out.println("TEST DELETE Destroy");
        return null;
    }

    @ApiRequest(action = HttpMethod.GET, route = "/api/talks/schedule")
    public HttpResponse allInASchedule(){
        System.out.println("TEST GET All in a schedule");
        return null;
    }
}
