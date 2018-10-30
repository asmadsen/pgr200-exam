package no.kristiania.pgr200.common.http;

import com.github.javafaker.Faker;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import org.junit.Test;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

public class HttpClientTest {
    private Faker faker = new Faker();
    private Gson gson = new Gson();

    @Test
    public void shouldThrowExceptionIfInvalidRequest() throws IOException {
        HttpClient client = mock(HttpClient.class);
        when(client.execute(anyString(), any(HttpRequest.class))).thenCallRealMethod();
        when(client.execute(anyString(), anyInt(), any(HttpRequest.class))).thenCallRealMethod();

        HttpRequest request = new HttpRequest();
        assertThatThrownBy(() -> {
            client.execute("httpbin.org", request);
        }).isInstanceOf(Exception.class);

        request.setUri("test");
        assertThatThrownBy(() -> {
            client.execute("httpbin.org", request);
        }).isInstanceOf(Exception.class);

        request.setUri("/");
        assertThatThrownBy(() -> {
            client.execute("httpbin.org", request);
        }).isInstanceOf(Exception.class);

        request.setHttpMethod(HttpMethod.GET);
        request.setHttpVersion(null);
        assertThatThrownBy(() -> {
            client.execute("httpbin.org", request);
        }).isInstanceOf(Exception.class);
    }

    @Test
    public void shouldCallMainMethodFromOverloadedOnes() throws IOException {
        HttpClient client = mock(HttpClient.class);
        when(client.execute(anyString(), any(HttpRequest.class))).thenCallRealMethod();
        HttpRequest request = new HttpRequest(HttpMethod.GET, "/");

        client.execute("httpbin.org", request);

        verify(client).execute(eq("httpbin.org"), eq(80), eq(request));
    }

    @Test
    public void shouldParseHostIntoPort() throws IOException {
        HttpClient client = mock(HttpClient.class);
        when(client.execute(anyString(), any(HttpRequest.class))).thenCallRealMethod();
        HttpRequest request = new HttpRequest(HttpMethod.GET, "/");

        client.execute("https://httpbin.org", request);
        verify(client).execute(eq("httpbin.org"), eq(443), eq(request));

        client.execute("https://httpbin.org:8080", request);
        verify(client).execute(eq("httpbin.org"), eq(8080), eq(request));
    }

    @Test
    public void shouldExecuteRequest() throws IOException {
        HttpClient client = new HttpClient();

        Map<String, String> headers = new HashMap<>();
        HttpRequest request = new HttpRequest(HttpMethod.GET, "/get", headers);

        HttpResponse response = client.execute("httpbin.org", request);
        JsonObject expect = new JsonObject();
        expect.add("args", new JsonObject());
        expect.add("headers", gson.toJsonTree(headers));
        expect.addProperty("url", "http://httpbin.org/get");
        JsonObject object = response.getJson().getAsJsonObject();
        object.remove("origin");
        assertThat(object.entrySet())
                .isEqualTo(expect.entrySet());
    }
}
