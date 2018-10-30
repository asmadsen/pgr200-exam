package no.kristiania.pgr200.server.controllers;

import com.google.gson.JsonObject;
import no.kristiania.pgr200.common.http.HttpMethod;
import no.kristiania.pgr200.common.http.HttpRequest;
import no.kristiania.pgr200.common.http.HttpResponse;
import no.kristiania.pgr200.common.http.HttpStatus;
import no.kristiania.pgr200.server.annotations.ApiController;
import no.kristiania.pgr200.server.annotations.ApiRequest;
import no.kristiania.pgr200.server.models.TalkModel;

@ApiController("/talks")
public class TalksController extends BaseController<TalkModel> {

    public TalksController(HttpRequest httpRequest){
        super(httpRequest);
    }

    @Override
    @ApiRequest(action = HttpMethod.GET, route = "/talks")
    public HttpResponse index() {
        return index(new TalkModel());
    }

    @Override
    @ApiRequest(action = HttpMethod.GET, route = "/talks" + uuidPath)
    public HttpResponse show() {
        return show(new TalkModel());
    }

    @Override
    @ApiRequest(action = HttpMethod.POST, route = "/talks")
    public HttpResponse create()  {
        return create(new TalkModel(getHttpRequest().getJson().getAsJsonObject()));
    }

    @Override
    @ApiRequest(action = HttpMethod.PUT, route = "/talks" + uuidPath)
    public HttpResponse update(){
        if(!validateUUID(getHttpRequest().getUri().split("/")[2])) {
            JsonObject error = new JsonObject();
            error.addProperty("error", "Not an UUID: " + getHttpRequest().getUri().split("/")[2]);
            addPropertyToBody("error", error);
            httpResponse.setStatus(HttpStatus.BadRequest);
            httpResponse.setBody(getResponsebody().toString());
            return httpResponse;
        }
        return update(new TalkModel(getUuidFromUri(),
                getHttpRequest().getJson().getAsJsonObject()));
    }

    @Override
    @ApiRequest(action = HttpMethod.DELETE, route = "/talks" + uuidPath)
    public HttpResponse destroy() {
        return destroy(new TalkModel(getUuidFromUri()));
    }
}
