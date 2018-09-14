package no.kristiania.pgr200.common.Http;

import com.google.gson.JsonObject;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;

import static no.kristiania.pgr200.common.Http.HttpMethod.*;
import static org.assertj.core.api.Assertions.assertThat;

public class HttpRequestTest {
    @Test
    public void shouldWriteRequestToOutputStream() throws IOException {
        HttpRequest request = new HttpRequest(GET, "/", "HelloWorld");

        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        request.writeToStream(stream);

        String actual = stream.toString();
        StringBuilder builder = new StringBuilder();
        builder.append("GET / HTTP/1.1\r\n");
        builder.append(String.format("Content-Length: %s\r\n", String.valueOf("HelloWorld".getBytes().length)));
        builder.append("\r\n");
        builder.append("HelloWorld");

        assertThat(actual).isEqualTo(builder.toString());
    }

    @Test
    public void shouldTestDifferentConstructors() throws Exception {
        HttpRequest request = new HttpRequest();
        assertThat(request)
                .hasFieldOrPropertyWithValue("uri", null)
                .hasFieldOrPropertyWithValue("httpMethod", null)
                .hasFieldOrPropertyWithValue("body", "")
                .hasFieldOrPropertyWithValue("json", null)
                .hasFieldOrPropertyWithValue("httpVersion", "HTTP/1.1");

        request = new HttpRequest(POST);
        assertThat(request)
                .hasFieldOrPropertyWithValue("uri", null)
                .hasFieldOrPropertyWithValue("httpMethod", POST)
                .hasFieldOrPropertyWithValue("body", "")
                .hasFieldOrPropertyWithValue("json", null)
                .hasFieldOrPropertyWithValue("httpVersion", "HTTP/1.1");

        request = new HttpRequest(PUT, "/");

        assertThat(request)
                .hasFieldOrPropertyWithValue("uri", "/")
                .hasFieldOrPropertyWithValue("httpMethod", PUT)
                .hasFieldOrPropertyWithValue("body", "")
                .hasFieldOrPropertyWithValue("json", null)
                .hasFieldOrPropertyWithValue("httpVersion", "HTTP/1.1");

        request = new HttpRequest(DELETE, "/path", "body");
        assertThat(request)
                .hasFieldOrPropertyWithValue("uri", "/path")
                .hasFieldOrPropertyWithValue("httpMethod", DELETE)
                .hasFieldOrPropertyWithValue("body", "body")
                .hasFieldOrPropertyWithValue("json", null)
                .hasFieldOrPropertyWithValue("httpVersion", "HTTP/1.1");

        request = new HttpRequest(DELETE, "/path", new HashMap<String, String>() {{
            put("Accept-Encoding", "gzip");
        }});
        assertThat(request)
                .hasFieldOrPropertyWithValue("uri", "/path")
                .hasFieldOrPropertyWithValue("httpMethod", DELETE)
                .hasFieldOrPropertyWithValue("body", "")
                .hasFieldOrPropertyWithValue("json", null)
                .hasFieldOrPropertyWithValue("httpVersion", "HTTP/1.1");
        assertThat(request.getHeaders()).containsEntry("Accept-Encoding", "gzip");

        JsonObject body = new JsonObject();
        body.addProperty("hello", "world");
        request = new HttpRequest(DELETE, "/path", body);
        assertThat(request)
                .hasFieldOrPropertyWithValue("uri", "/path")
                .hasFieldOrPropertyWithValue("httpMethod", DELETE)
                .hasFieldOrPropertyWithValue("body", body.toString())
                .hasFieldOrPropertyWithValue("json", body)
                .hasFieldOrPropertyWithValue("httpVersion", "HTTP/1.1");
        assertThat(request.getHeaders()).containsEntry("Content-Type", "application/json");

        body = new JsonObject();
        body.addProperty("hello", "world");
        request = new HttpRequest(DELETE, "/path", new HashMap<String, String>() {{
            put("Accept-Encoding", "gzip");
        }}, body);
        assertThat(request)
                .hasFieldOrPropertyWithValue("uri", "/path")
                .hasFieldOrPropertyWithValue("httpMethod", DELETE)
                .hasFieldOrPropertyWithValue("body", body.toString())
                .hasFieldOrPropertyWithValue("json", body)
                .hasFieldOrPropertyWithValue("httpVersion", "HTTP/1.1");
        assertThat(request.getHeaders()).containsEntry("Accept-Encoding", "gzip")
                                        .containsEntry("Content-Type", "application/json");

        request = new HttpRequest(DELETE, "/path", new HashMap<String, String>() {{
            put("Accept-Encoding", "gzip");
        }}, "body");
        assertThat(request)
                .hasFieldOrPropertyWithValue("uri", "/path")
                .hasFieldOrPropertyWithValue("httpMethod", DELETE)
                .hasFieldOrPropertyWithValue("body", "body")
                .hasFieldOrPropertyWithValue("json", null)
                .hasFieldOrPropertyWithValue("httpVersion", "HTTP/1.1");
        assertThat(request.getHeaders()).containsEntry("Accept-Encoding", "gzip");
    }
}
