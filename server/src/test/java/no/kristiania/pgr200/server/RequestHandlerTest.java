package no.kristiania.pgr200.server;

import no.kristiania.pgr200.common.Http.HttpMethod;
import no.kristiania.pgr200.common.Http.HttpRequest;
import no.kristiania.pgr200.server.controllers.BaseController;
import no.kristiania.pgr200.server.controllers.ScheduleController;
import no.kristiania.pgr200.server.controllers.TalksController;
import org.junit.Test;

import java.lang.reflect.Method;
import java.util.HashMap;

import static org.assertj.core.api.Assertions.assertThat;

public class RequestHandlerTest {

    @Test
    public void shouldReturnTalksController(){
        RequestHandler requestHandler = new RequestHandler(new HttpRequest(HttpMethod.GET, "/api/talks", defaultHeaders()));
        BaseController controller = requestHandler.getControllerFromRoute();
        assertThat(controller).isInstanceOf(TalksController.class);
    }

    @Test
    public void shouldReturnScheduleController(){
        RequestHandler requestHandler = new RequestHandler( new HttpRequest(HttpMethod.GET, "/api/schedule", defaultHeaders()));
        BaseController controller = requestHandler.getControllerFromRoute();
        assertThat(controller).isInstanceOf(ScheduleController.class);
    }

    @Test
    public void shouldReturnIndexMethod() throws NoSuchMethodException {
        String[] requestCases = { "/api/talks/", "/api/talks", "/api/talks?title=sometitle" };
        loopCases(requestCases, HttpMethod.GET, "index");
    }

    @Test
    public void shouldReturnShowMethod() throws NoSuchMethodException {
        String[] requestCases = { "/api/talks/1", "/api/talks/123", "/api/talks/123notAnId" };
        loopCases(requestCases, HttpMethod.GET, "show");
    }

    @Test
    public void shouldInvokeCreateMethod() throws NoSuchMethodException {
        String[] requestCases = { "/api/talks", "/api/talks", "/api/talks" };
        loopCases(requestCases, HttpMethod.POST, "create");
    }

    @Test
    public void shouldInvokeUpdateMethod() throws NoSuchMethodException {
        String[] requestCases = { "/api/talks/1", "/api/talks/123", "/api/talks/123notAnId" };
        loopCases(requestCases, HttpMethod.PATCH, "update");
    }

    @Test
    public void shouldReturnDestroyMethod() throws NoSuchMethodException {
        String[] requestCases = { "/api/talks/1", "/api/talks/123", "/api/talks/123notAnId" };
        loopCases(requestCases, HttpMethod.DELETE, "destroy");
    }

    private void loopCases(String[] route, HttpMethod httpMethod, String method) throws NoSuchMethodException {
        for (int i = 0; i < route.length-1; i++) {
            RequestHandler requestHandler = new RequestHandler( new HttpRequest(httpMethod, route[i], defaultHeaders()));
            BaseController controller = requestHandler.getControllerFromRoute();
            Method correctMethod = controller.getClass().getMethod(method);
            Method requestedMethod = requestHandler.getMethodFromAnnotation(controller);
            assertThat(requestedMethod).isEqualTo(correctMethod);
        }
    }

    private HashMap<String, String> defaultHeaders() {
        HashMap<String, String> headers = new HashMap<>();
        headers.put("Host:", "localhost:8080");
        headers.put("Content-Type:", "application/x-www-form-urlencoded");
        headers.put("Connection:", "close");
        return headers;
    }
}
