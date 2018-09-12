package no.kristiania.pgr200.server;


import com.google.gson.Gson;
import no.kristiania.pgr200.common.Http.HttpMethod;

import java.lang.reflect.Method;
import java.sql.SQLException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@ApiController("/talks")
public class TalksController extends BaseController{

    @ApiRequest(action = HttpMethod.GET, route = "/api/talks")
    public void index() {
        System.out.println("TEST GET Index");
        sendSuccessResponse(getOut(), "application/json");
    }

    @ApiRequest(action = HttpMethod.GET, route = "/api/talks/\\d+")
    public void show() throws SQLException {
        System.out.println("TEST GET Show");
        setId(getRoute().split("/")[3]);
        TalkResponse talkResponse = new TalkResponse();
        Talk talk = talkResponse.fetchTalkById(getId());
        if (talk.getTitle() == null && talk.getDescription() == null){
            HttpErrorCodes message = new HttpErrorCodes("422", getId());
            sendUnprocessableResponse(getOut(), new Gson().toJson(message));
        } else {
            sendSuccessResponse(getOut(), "application/json");
            getOut().println(new Gson().toJson(talk));
            getOut().flush();
        }
    }

    @ApiRequest(action = HttpMethod.POST, route = "/api/talks")
    public void create(){
        System.out.println("TEST POST Create");
    }

    @ApiRequest(action = HttpMethod.PATCH, route = "/api/talks/\\d+")
    public void update(){
        System.out.println("TEST PATCH Update");
    }

    @ApiRequest(action = HttpMethod.DELETE, route = "/api/talks/\\d+")
    public void destroy(){
        System.out.println("TEST DELETE Destroy");
    }

    @ApiRequest(action = HttpMethod.GET, route = "/api/talks/schedule")
    public void allInASchedule(){
        System.out.println("TEST GET All in a schedule");
        sendSuccessResponse(getOut(), "application/json");
        getOut().println("{\"test\":\"alltalks\"}");
        getOut().flush();
    }
}
