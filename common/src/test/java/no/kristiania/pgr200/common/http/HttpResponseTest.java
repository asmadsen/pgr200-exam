package no.kristiania.pgr200.common.http;

import com.google.gson.JsonObject;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;

import static no.kristiania.pgr200.common.http.HttpStatus.*;
import static org.assertj.core.api.Assertions.assertThat;

public class HttpResponseTest {
    @Test
    public void shouldWriteRequestToOutputStream() throws IOException {
        HttpResponse response = new HttpResponse(OK, "HelloWorld");

        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        response.writeToStream(stream);

        String actual = stream.toString();
        StringBuilder builder = new StringBuilder();
        builder.append("HTTP/1.1 200 OK\r\n");
        builder.append(String.format("Content-Length: %s\r\n", String.valueOf("HelloWorld".getBytes().length)));
        builder.append("\r\n");
        builder.append("HelloWorld");

        assertThat(actual).isEqualTo(builder.toString());
    }

    @Test
    public void shouldTestDifferentConstructors() throws Exception {
        HttpResponse response = new HttpResponse();
        assertThat(response)
                .hasFieldOrPropertyWithValue("body", "")
                .hasFieldOrPropertyWithValue("json", null)
                .hasFieldOrPropertyWithValue("httpVersion", "HTTP/1.1");

        response = new HttpResponse(ImaTeapot);
        assertThat(response)
                .hasFieldOrPropertyWithValue("body", "")
                .hasFieldOrPropertyWithValue("json", null)
                .hasFieldOrPropertyWithValue("httpVersion", "HTTP/1.1");

        response = new HttpResponse(NonAuthoritativeInformation, "HelloWorld");

        assertThat(response)
                .hasFieldOrPropertyWithValue("status", NonAuthoritativeInformation)
                .hasFieldOrPropertyWithValue("body", "HelloWorld")
                .hasFieldOrPropertyWithValue("json", null)
                .hasFieldOrPropertyWithValue("httpVersion", "HTTP/1.1");

        response = new HttpResponse(OK, new HashMap<String, String>() {{
            put("Content-Encoding", "gzip");
        }});
        assertThat(response)
                .hasFieldOrPropertyWithValue("status", OK)
                .hasFieldOrPropertyWithValue("body", "")
                .hasFieldOrPropertyWithValue("json", null)
                .hasFieldOrPropertyWithValue("httpVersion", "HTTP/1.1");
        assertThat(response.getHeaders()).containsEntry("Content-Encoding", "gzip");

        response = new HttpResponse(OK, new HashMap<String, String>() {{
            put("Content-Encoding", "gzip");
        }}, "HelloWorld");
        assertThat(response)
                .hasFieldOrPropertyWithValue("body", "HelloWorld")
                .hasFieldOrPropertyWithValue("json", null)
                .hasFieldOrPropertyWithValue("httpVersion", "HTTP/1.1");
        assertThat(response.getHeaders()).containsEntry("Content-Encoding", "gzip");

        JsonObject body = new JsonObject();
        body.addProperty("hello", "world");
        response = new HttpResponse(OK, body);
        assertThat(response)
                .hasFieldOrPropertyWithValue("status", OK)
                .hasFieldOrPropertyWithValue("body", body.toString())
                .hasFieldOrPropertyWithValue("json", body)
                .hasFieldOrPropertyWithValue("httpVersion", "HTTP/1.1");
        assertThat(response.getHeaders()).containsEntry("Content-Type", "application/json");

        body = new JsonObject();
        body.addProperty("hello", "world");
        response = new HttpResponse(OK, new HashMap<String, String>() {{
            put("Accept-Encoding", "gzip");
        }}, body);
        assertThat(response)
                .hasFieldOrPropertyWithValue("status", OK)
                .hasFieldOrPropertyWithValue("body", body.toString())
                .hasFieldOrPropertyWithValue("json", body)
                .hasFieldOrPropertyWithValue("httpVersion", "HTTP/1.1");
        assertThat(response.getHeaders()).containsEntry("Accept-Encoding", "gzip")
                                         .containsEntry("Content-Type", "application/json");
    }
}
