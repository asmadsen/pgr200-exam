package no.kristiania.pgr200.server;

import org.junit.Test;

import java.lang.reflect.Method;
import java.util.HashMap;

import static org.assertj.core.api.Assertions.assertThat;

public class RequestHandlerTest {

    @Test
    public void shouldReturnTalksController(){
        RequestHandler requestHandler = new RequestHandler("GET /api/talks HTTP/1.1", defaultHeaders(), "");
        BaseController controller = requestHandler.getControllerFromRoute();
        assertThat(controller).isInstanceOf(TalksController.class);
    }

    @Test
    public void shouldReturnScheduleController(){
        RequestHandler requestHandler = new RequestHandler("GET /api/schedule HTTP/1.1", defaultHeaders(), "");
        BaseController controller = requestHandler.getControllerFromRoute();
        assertThat(controller).isInstanceOf(ScheduleController.class);
    }

    @Test
    public void shouldReturnIndexMethod() throws NoSuchMethodException {
        String[] requestCases = { "GET /api/talks/ HTTP/1.1", "GET /api/talks HTTP/1.1", "GET /api/talks?title=sometitle HTTP/1.1" };
        loopCases(requestCases, "index");
    }

    @Test
    public void shouldReturnShowMethod() throws NoSuchMethodException {
        String[] requestCases = { "GET /api/talks/1 HTTP/1.1", "GET /api/talks/123 HTTP/1.1", "GET /api/talks/123notAnId HTTP/1.1" };
        loopCases(requestCases, "show");
    }

    @Test
    public void shouldInvokeCreateMethod() throws NoSuchMethodException {
        String[] requestCases = { "POST /api/talks HTTP/1.1", "POST /api/talks HTTP/1.1", "POST /api/talks HTTP/1.1" };
        loopCases(requestCases, "create");
    }

    @Test
    public void shouldInvokeUpdateMethod() throws NoSuchMethodException {
        String[] requestCases = { "PATCH /api/talks/1 HTTP/1.1", "PATCH /api/talks/123 HTTP/1.1", "PATCH /api/talks/123notAnId HTTP/1.1" };
        loopCases(requestCases, "update");
    }

    @Test
    public void shouldReturnDestroyMethod() throws NoSuchMethodException {
        String[] requestCases = { "DELETE /api/talks/1 HTTP/1.1", "DELETE /api/talks/123 HTTP/1.1", "DELETE /api/talks/123notAnId HTTP/1.1" };
        loopCases(requestCases, "destroy");
    }

    private void loopCases(String[] requestCases, String method) throws NoSuchMethodException {
        for (int i = 0; i < requestCases.length-1; i++) {
            RequestHandler requestHandler = new RequestHandler(requestCases[i], defaultHeaders(), "");
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
