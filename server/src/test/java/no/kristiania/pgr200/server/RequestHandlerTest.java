package no.kristiania.pgr200.server;

import no.kristiania.pgr200.common.http.HttpMethod;
import no.kristiania.pgr200.common.http.HttpRequest;
import no.kristiania.pgr200.server.controllers.BaseController;
import no.kristiania.pgr200.server.controllers.TalksController;
import org.junit.Test;

import java.lang.reflect.Method;
import java.util.HashMap;

import static org.assertj.core.api.Assertions.assertThat;

public class RequestHandlerTest {

    @Test
    public void shouldReturnTalksController() {
        RequestHandler requestHandler = new RequestHandler(new HttpRequest(HttpMethod.GET,
                                                                           "/api/talks",
                                                                           defaultHeaders()));
        BaseController controller = requestHandler.getControllerFromRoute();
        assertThat(controller).isInstanceOf(TalksController.class);
    }

    @Test
    public void shouldReturnIndexMethod() throws NoSuchMethodException {
        String[] requestCases = {"/talks/", "/talks", "/talks?title=sometitle"};
        loopCases(requestCases, HttpMethod.GET, "index");
    }

    @Test
    public void shouldReturnShowMethod() throws NoSuchMethodException {
        String[] requestCases = {"/talks/1", "/talks/123", "/talks/123notAnId"};
        loopCases(requestCases, HttpMethod.GET, "show");
    }

    @Test
    public void shouldInvokeCreateMethod() throws NoSuchMethodException {
        String[] requestCases = {"/talks", "/talks", "/talks"};
        loopCases(requestCases, HttpMethod.POST, "create");
    }

    @Test
    public void shouldInvokeUpdateMethod() throws NoSuchMethodException {
        String[] requestCases = {"/talks/1", "/talks/123", "/talks/123notAnId"};
        loopCases(requestCases, HttpMethod.PUT, "update");
    }

    @Test
    public void shouldReturnDestroyMethod() throws NoSuchMethodException {
        String[] requestCases = {"/talks/1", "/talks/123", "/talks/123notAnId"};
        loopCases(requestCases, HttpMethod.DELETE, "destroy");
    }

    private void loopCases(String[] route, HttpMethod httpMethod, String method) throws NoSuchMethodException {
        for (int i = 0; i < route.length - 1; i++) {
            RequestHandler requestHandler = new RequestHandler(new HttpRequest(httpMethod, route[i], defaultHeaders()));
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
