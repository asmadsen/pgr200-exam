package no.kristiania.pgr200.common.Http;

import com.github.javafaker.Faker;
import com.google.gson.JsonObject;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;


public class HttpParserTest {
    private HttpParser parser = new HttpParser();
    private Faker faker = new Faker();

    @Test
    public void shouldParseStatusLineInResponse() throws IOException {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        stream.write("HTTP/1.1 200 OK\r\n".getBytes());
        stream.write("\r\n".getBytes());
        stream.flush();

        HttpResponse response = parser.parseResponse(new ByteArrayInputStream(stream.toByteArray()));

        assertThat(response).hasFieldOrPropertyWithValue("httpVersion", "HTTP/1.1");
        assertThat(response).hasFieldOrPropertyWithValue("status", HttpStatus.OK);
    }

    @Test
    public void shouldParseRequestLine() throws IOException {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        stream.write("GET / HTTP/1.1\r\n".getBytes());
        stream.write("\r\n".getBytes());
        stream.flush();

        HttpRequest response = parser.parseRequest(new ByteArrayInputStream(stream.toByteArray()));

        assertThat(response).hasFieldOrPropertyWithValue("httpMethod", HttpMethod.GET);
        assertThat(response).hasFieldOrPropertyWithValue("uri", "/");
        assertThat(response).hasFieldOrPropertyWithValue("httpVersion", "HTTP/1.1");
    }

    @Test
    public void shouldParseDifferentStatuses() throws IOException {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        stream.write("HTTP/1.1 404 NOT FOUND\r\n".getBytes());
        stream.write("\r\n".getBytes());
        stream.flush();

        HttpResponse response = parser.parseResponse(new ByteArrayInputStream(stream.toByteArray()));

        assertThat(response).hasFieldOrPropertyWithValue("httpVersion", "HTTP/1.1");
        assertThat(response).hasFieldOrPropertyWithValue("status", HttpStatus.NotFound);
    }

    @Test
    public void shouldParseHeadersIntoMap() throws IOException {
        String etag = faker.crypto().sha1();
        String date = faker.date().future(3, TimeUnit.DAYS).toString();
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        stream.write("HTTP/1.1 200 OK\r\n".getBytes());
        stream.write("Content-Type: text/html\r\n".getBytes());
        stream.write(String.format("etag: %s\r\n", etag).getBytes());
        stream.write(String.format("date: %s\r\n", date).getBytes());
        stream.write("\r\n".getBytes());
        stream.flush();

        HttpResponse response = parser.parseResponse(new ByteArrayInputStream(stream.toByteArray()));

        assertThat(response.getHeaders()).containsAllEntriesOf(new HashMap<String, String>() {{
            put("Content-Type", "text/html");
            put("Etag", etag);
            put("Date", date);
        }});
    }

    @Test
    public void shouldParseBodyAsTextIfTypeUnknown() throws IOException {
        String body = faker.rickAndMorty().quote();
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        stream.write("HTTP/1.1 200 OK\r\n".getBytes());
        //stream.write("Content-Type: text/html\r\n".getBytes());
        stream.write(String.format("Content-Length: %s\r\n", body.getBytes().length).getBytes());
        stream.write("\r\n".getBytes());
        stream.write(body.getBytes());
        stream.flush();

        HttpResponse response = parser.parseResponse(new ByteArrayInputStream(stream.toByteArray()));

        assertThat(response).hasFieldOrPropertyWithValue("body", body);
        assertThat(response).hasFieldOrPropertyWithValue("json", null);
    }

    @Test
    public void shouldParseBodyAsJsonIfApplicationJson() throws IOException {
        JsonObject body = new JsonObject();
        body.addProperty("rick", faker.rickAndMorty().quote());
        body.addProperty("yoda", faker.yoda().quote());
        body.addProperty("chuckNorris", faker.chuckNorris().fact());
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        stream.write("HTTP/1.1 200 OK\r\n".getBytes());
        stream.write("Content-Type: application/json\r\n".getBytes());
        stream.write(String.format("Content-Length: %s\r\n", body.toString().getBytes(StandardCharsets.UTF_8).length)
                           .getBytes());
        stream.write("\r\n".getBytes());
        stream.write(body.toString().getBytes(StandardCharsets.UTF_8));
        stream.flush();

        HttpResponse response = parser.parseResponse(new ByteArrayInputStream(stream.toByteArray()));

        assertThat(response).hasFieldOrPropertyWithValue("body", body.toString());
        assertThat(response).hasFieldOrPropertyWithValue("json", body);
    }
}
