package no.kristiania.pgr200.server.controllers;


import com.google.gson.*;
import jdk.nashorn.internal.ir.annotations.Ignore;
import no.kristiania.pgr200.common.Http.HttpMethod;
import no.kristiania.pgr200.common.Http.HttpRequest;
import no.kristiania.pgr200.common.Http.HttpResponse;
import no.kristiania.pgr200.common.Http.HttpStatus;
import no.kristiania.pgr200.server.annotations.ApiController;
import no.kristiania.pgr200.server.annotations.ApiRequest;
import no.kristiania.pgr200.server.models.TalkModel;

import java.sql.SQLException;
import java.util.UUID;

@ApiController("/talks")
public class TalksController extends BaseController {

    private HttpRequest httpRequest;
    private HttpResponse httpResponse;
    private Gson gson;
    private JsonObject body;

    public TalksController(HttpRequest httpRequest){
        this.httpRequest = httpRequest;
        this.httpResponse = new HttpResponse();
        this.body = new JsonObject();
        this.gson = new GsonBuilder().setPrettyPrinting().create();
    }

    @ApiRequest(action = HttpMethod.GET, route = "/api/talks")
    public HttpResponse index() throws Exception {
        body.add("values", new JsonParser().parse(gson.toJson(new TalkModel().all())));
        httpResponse.setHeaders(getHeaders(gson.toJson(body)));
        httpResponse.setBody(gson.toJson(body));
        httpResponse.setStatus(HttpStatus.OK);
        return httpResponse;
    }

    @ApiRequest(action = HttpMethod.GET, route = "/api/talks/[0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}")
    public HttpResponse show() throws Exception {
        body.add("value", new JsonParser().parse(gson.toJson(
                new TalkModel().findById(UUID.fromString(httpRequest.getUri().split("/")[3])))));
        httpResponse.setHeaders(getHeaders(gson.toJson(body)));
        httpResponse.setBody(gson.toJson(body));
        httpResponse.setStatus(HttpStatus.OK);
        return httpResponse;
    }

    @ApiRequest(action = HttpMethod.POST, route = "/api/talks")
    public HttpResponse create( ) throws SQLException {
        TalkModel model = new TalkModel(httpRequest.getJson().getAsJsonObject());
        if (model.create()) {
            body.add("value", new JsonParser().parse(gson.toJson(model.getState())));
        } else{
            return new HttpResponse(HttpStatus.UnprocessableEntity);
        }
        return new HttpResponse(HttpStatus.Created, body);
    }

    @ApiRequest(action = HttpMethod.PUT, route = "/api/talks/[0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}")
    public HttpResponse update(){
        TalkModel model = new TalkModel(httpRequest.getUri().split("/")[3], httpRequest.getJson().getAsJsonObject());
        if(model.save()){
            body.add("value", new JsonParser().parse(gson.toJson(model.getState())));
        } else {
            return new HttpResponse(HttpStatus.UnprocessableEntity);
        }
        return new HttpResponse(HttpStatus.OK, body);
    }

    @ApiRequest(action = HttpMethod.DELETE, route = "/api/talks/[0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}")
    public HttpResponse destroy() throws SQLException {
        TalkModel model = new TalkModel(httpRequest.getUri().split("/")[3], httpRequest.getJson().getAsJsonObject());
        if(model.destroy()){
            return new HttpResponse(HttpStatus.NoContent);
        } else {
            return new HttpResponse(HttpStatus.BadRequest);
        }
    }

    @ApiRequest(action = HttpMethod.GET, route = "/api/talks/schedule")
    public HttpResponse allInASchedule(){
        System.out.println("TEST GET All in a schedule");
        return null;
    }
}
