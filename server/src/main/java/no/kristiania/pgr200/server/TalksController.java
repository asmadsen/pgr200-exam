package no.kristiania.pgr200.server;


import com.google.gson.Gson;

import java.sql.SQLException;

public class TalksController extends BaseController{

    @ApiRequest(action = "GET", route = "talks")
    public void index() throws SQLException {
        System.out.println("TEST GET Index");
        System.out.println(getParameters());

    }

    @ApiRequest(action = "POST", route = "talks")
    public void create(){
        System.out.println("TEST POST Create");
    }

    @ApiRequest(action = "GET", route = "talks/\\d+(?!\\S+)")
    public void show() throws SQLException {
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

    @ApiRequest(action = "PATCH", route = "talks")
    public void update(){
        System.out.println("TEST PATCH Update");
    }

    @ApiRequest(action = "DELETE", route = "talks//\\d+(?!\\S+)")
    public void destroy(){
        System.out.println("TEST DELETE Destroy");
    }
}
