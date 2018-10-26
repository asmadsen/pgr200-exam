package no.kristiania.pgr200.server.controllers;


import com.google.gson.JsonParser;
import no.kristiania.pgr200.common.Http.HttpMethod;
import no.kristiania.pgr200.common.Http.HttpRequest;
import no.kristiania.pgr200.common.Http.HttpResponse;
import no.kristiania.pgr200.common.Http.HttpStatus;
import no.kristiania.pgr200.server.annotations.ApiController;
import no.kristiania.pgr200.server.annotations.ApiRequest;
import no.kristiania.pgr200.server.models.TalkModel;

import java.util.UUID;

@ApiController("/talks")
public class TalksController extends BaseController {

    public TalksController(HttpRequest httpRequest){
        super(httpRequest);

    }

    @Override
    @ApiRequest(action = HttpMethod.GET, route = "/talks")
    public HttpResponse index() {
        getBody().add("values", new JsonParser().parse(getGsonBuilder().toJson(new TalkModel().all())));
        httpResponse.setHeaders(getHeaders(getGsonBuilder().toJson(getBody())));
        httpResponse.setBody(getGsonBuilder().toJson(getBody()));
        httpResponse.setStatus(HttpStatus.OK);
        return httpResponse;
    }

    @Override
    @ApiRequest(action = HttpMethod.GET, route = "/talks" + uuidPath)
    public HttpResponse show() {
        if(!validateUUID(getHttpRequest().getUri().split("/")[2])) {
            getBody().add("error", getErrorMessage("Could not find Talk with id: " + getHttpRequest().getUri().replaceFirst("/", "")));
            return new HttpResponse(HttpStatus.BadRequest, getBody());
        }
        getBody().add("value", new JsonParser().parse(getGsonBuilder().toJson(
                new TalkModel().findById(UUID.fromString(getHttpRequest().getUri().split("/")[2])))));
        httpResponse.setHeaders(getHeaders(getGsonBuilder().toJson(getBody())));
        httpResponse.setBody(getGsonBuilder().toJson(getBody()));
        httpResponse.setStatus(HttpStatus.OK);
        return httpResponse;
    }

    @Override
    @ApiRequest(action = HttpMethod.POST, route = "/talks")
    public HttpResponse create( )  {
        TalkModel model = new TalkModel(getHttpRequest().getJson().getAsJsonObject());
        if (model.create()) {
            getBody().add("value", new JsonParser().parse(getGsonBuilder().toJson(model.getState())));
        } else{
            return new HttpResponse(HttpStatus.UnprocessableEntity);
        }
        return new HttpResponse(HttpStatus.Created, getBody());
    }

    @Override
    @ApiRequest(action = HttpMethod.PUT, route = "/talks" + uuidPath)
    public HttpResponse update(){
        TalkModel model = new TalkModel(getHttpRequest().getUri().split("/")[1], getHttpRequest().getJson().getAsJsonObject());
        if(model.save()){
            getBody().add("value", new JsonParser().parse(getGsonBuilder().toJson(model.getState())));
        } else {
            return new HttpResponse(HttpStatus.UnprocessableEntity);
        }
        return new HttpResponse(HttpStatus.OK, getBody());
    }

    @Override
    @ApiRequest(action = HttpMethod.DELETE, route = "/talks" + uuidPath)
    public HttpResponse destroy() {
        TalkModel model = new TalkModel(getHttpRequest().getUri().split("/")[1], getHttpRequest().getJson().getAsJsonObject());
        if(model.destroy()){
            return new HttpResponse(HttpStatus.NoContent);
        } else {
            return new HttpResponse(HttpStatus.BadRequest);
        }
    }

    @ApiRequest(action = HttpMethod.GET, route = "/schedule")
    public HttpResponse allInASchedule(){
        System.out.println("TEST GET All in a schedule");
        return null;
    }
}
