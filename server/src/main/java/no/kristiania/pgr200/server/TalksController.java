package no.kristiania.pgr200.server;


import com.google.gson.Gson;
import no.kristiania.pgr200.common.Http.HttpMethod;

import java.lang.reflect.Method;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

@ApiController("/talks")
public class TalksController extends BaseController{

    @ApiRequest(action = HttpMethod.GET, route = "/")
    public void index() {
        System.out.println("TEST GET Index");
        System.out.println(getRoute());
    }

    @ApiRequest(action = HttpMethod.GET, route = "/{id:\\d+}")
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

    @ApiRequest(action = HttpMethod.POST, route = "/")
    public void create(){
        System.out.println("TEST POST Create");
    }

    @ApiRequest(action = HttpMethod.PATCH, route = "/{id:\\d+}")
    public void update(){
        System.out.println("TEST PATCH Update");
    }

    @ApiRequest(action = HttpMethod.DELETE, route = "/{id:\\d+}")
    public void destroy(){
        System.out.println("TEST DELETE Destroy");
    }

    @Override
    public Method parseRoute(HttpMethod method, String route){
        Pattern regex = Pattern.compile("\\{([a-zA-Z_][a-zA-Z0-9_-]*)(?::([^{}]*(?:[^{}]*)*))?}");
        List<Method> methods = Arrays.asList(getClass().getDeclaredMethods());
        final Method[] action = {null};
        System.out.println(methods.size());
        for (Method e : methods) {
            System.out.println(e);
            if (e.isAnnotationPresent(ApiRequest.class)) {
//                Matcher matcher = regex.matcher(e.getAnnotation(ApiRequest.class).route());
//                if (matcher.find() && method.equals(e.getAnnotation(ApiRequest.class).action())) {
//                    action[0] = e;
//                    break;
//                }
            }
        }
        return action[0];
    }
}
