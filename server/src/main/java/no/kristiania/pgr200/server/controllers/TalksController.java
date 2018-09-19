package no.kristiania.pgr200.server.controllers;


import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import no.kristiania.pgr200.common.Http.HttpMethod;
import no.kristiania.pgr200.common.Http.HttpRequest;
import no.kristiania.pgr200.common.Http.HttpResponse;
import no.kristiania.pgr200.common.Http.HttpStatus;
import no.kristiania.pgr200.server.HttpErrorCodes;
import no.kristiania.pgr200.server.TalkResponse;
import no.kristiania.pgr200.server.annotations.ApiRequest;
import no.kristiania.pgr200.server.controllers.ApiController;
import no.kristiania.pgr200.server.controllers.BaseController;
import no.kristiania.pgr200.server.models.Talk;

import java.sql.SQLException;

@ApiController("/talks")
public class TalksController extends BaseController {

    HttpRequest httpRequest;

    public TalksController(HttpRequest httpRequest){
        this.httpRequest = httpRequest;
    }

    @ApiRequest(action = HttpMethod.GET, route = "/api/talks")
    public HttpResponse index() throws SQLException {
        System.out.println("TEST GET Index");
        TalkResponse talkResponse = new TalkResponse();
        talkResponse.fetchAllTalks();
        if (talkResponse.getTalks().isEmpty()){
            sendUnprocessableResponse(getOut(), "message");
        } else {
            Gson gson = new Gson();
            String json = gson.toJson(talkResponse);
            JsonObject obj = new JsonParser().parse(json).getAsJsonObject();
//            obj.get("Talks").getAsJsonArray().forEach(e -> e.getAsJsonObject().remove("description"));
            return new HttpResponse(HttpStatus.OK, obj);
        }
        return null;
    }

    @ApiRequest(action = HttpMethod.GET, route = "/api/talks/\\d+")
    public HttpResponse show() throws SQLException {
        System.out.println("TEST GET Show");
        setId(httpRequest.getUri().split("/")[3]);
        TalkResponse talkResponse = new TalkResponse();
        Talk talk = talkResponse.fetchTalkById(getId());
        if (talk.getTitle() == null && talk.getDescription() == null){
            HttpErrorCodes message = new HttpErrorCodes("422", getId());
            sendUnprocessableResponse(getOut(), new Gson().toJson(message));
        } else {
//            sendSuccessResponse(getOut(), "application/json");
//            getOut().println(new Gson().toJson(talk));
//            getOut().flush();
            return new HttpResponse(HttpStatus.OK, new Gson().toJson(talkResponse));
        }
        return null;
    }

    @ApiRequest(action = HttpMethod.POST, route = "/api/talks")
    public HttpResponse create( ) throws SQLException {
        System.out.println("TEST POST Create");
        TalkResponse talkResponse = new TalkResponse();
        JsonElement jsonElement = talkResponse.createTalk(new Talk(httpRequest.getJson()));
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
        sendSuccessResponse(getOut(), "application/json");
        getOut().println("{\"test\":\"alltalks\"}");
        getOut().flush();
        return null;
    }
}
