package no.kristiania.pgr200.server;

import com.github.javafaker.Faker;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import no.kristiania.pgr200.common.Http.HttpMethod;
import no.kristiania.pgr200.common.Http.HttpParser;
import no.kristiania.pgr200.common.Http.HttpResponse;
import no.kristiania.pgr200.common.Http.HttpStatus;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.sql.SQLException;
import java.util.HashMap;

import static no.kristiania.pgr200.server.SetupDatabaseTest.initSchema;
import static org.assertj.core.api.Assertions.assertThat;

public class RequestHandlerIT {

    @BeforeClass
    public static void setupDatabase(){
        ConferenceServer.DATABASE_URL = "command_line_parser_test";
        initSchema();
    }

    @Test
    public void shouldInvokeIndexMethod() throws InvocationTargetException, IllegalAccessException, IOException {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        String[] requestCases = { "GET /api/talks/ HTTP/1.1","GET /api/talks HTTP/1.1" };

        RequestHandler requestHandler1 = new RequestHandler(requestCases[0], defaultHeaders(), "");
        requestHandler1.processRequest(new PrintWriter(stream));

        HttpParser httpParser = new HttpParser();
        HttpResponse response1 = httpParser.parseResponse(new ByteArrayInputStream(stream.toByteArray()));
        assertThat(response1).hasFieldOrPropertyWithValue("httpVersion", "HTTP/1.1");
        assertThat(response1).hasFieldOrPropertyWithValue("status", HttpStatus.OK);
        assertThat(requestHandler1.getController()).hasFieldOrPropertyWithValue("method", HttpMethod.GET);
        assertThat(requestHandler1.getController()).hasFieldOrPropertyWithValue("route", "/api/talks/");

        stream = new ByteArrayOutputStream();
        RequestHandler requestHandler2 = new RequestHandler(requestCases[1], defaultHeaders(), "");
        requestHandler2.processRequest(new PrintWriter(stream));
        HttpResponse response2 = httpParser.parseResponse(new ByteArrayInputStream(stream.toByteArray()));
        assertThat(response2).hasFieldOrPropertyWithValue("httpVersion", "HTTP/1.1");
        assertThat(response2).hasFieldOrPropertyWithValue("status", HttpStatus.OK);
        assertThat(requestHandler2.getController()).hasFieldOrPropertyWithValue("method", HttpMethod.GET);
        assertThat(requestHandler2.getController()).hasFieldOrPropertyWithValue("route", "/api/talks");
    }

    @Test
    public void shouldInvokeShowMethod() throws InvocationTargetException, IllegalAccessException, IOException, SQLException {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        int id = new Faker().number().numberBetween(0, 10000);
        String talk = new TalkResponse().createTalk(new Talk("title", "description"));
        String talkId = new Gson().fromJson(talk, JsonObject.class).get("Values").getAsJsonObject().get("id").toString().replaceAll("\"", "");
        String[] requestCases = {"GET /api/talks/" + id + " HTTP/1.1", "GET /api/talks/" + talkId + " HTTP/1.1"};

        System.out.println(talkId);

        RequestHandler requestHandler1 = new RequestHandler(requestCases[0], defaultHeaders(), "");
        requestHandler1.processRequest(new PrintWriter(stream));

        HttpParser httpParser = new HttpParser();
        HttpResponse response1 = httpParser.parseResponse(new ByteArrayInputStream(stream.toByteArray()));
        assertThat(response1).hasFieldOrPropertyWithValue("httpVersion", "HTTP/1.1");
        assertThat(response1).hasFieldOrPropertyWithValue("status", HttpStatus.UnprocessableEntity);
        assertThat(requestHandler1.getController()).hasFieldOrPropertyWithValue("method", HttpMethod.GET);
        assertThat(requestHandler1.getController()).hasFieldOrPropertyWithValue("id", String.valueOf(id));
        assertThat(requestHandler1.getController()).hasFieldOrPropertyWithValue("route", "/api/talks/" + String.valueOf(id));

        stream = new ByteArrayOutputStream();
        RequestHandler requestHandler2 = new RequestHandler(requestCases[1], defaultHeaders(), "");
        requestHandler2.processRequest(new PrintWriter(stream));
        HttpResponse response2 = httpParser.parseResponse(new ByteArrayInputStream(stream.toByteArray()));
        assertThat(response2).hasFieldOrPropertyWithValue("httpVersion", "HTTP/1.1");
        assertThat(response2).hasFieldOrPropertyWithValue("status", HttpStatus.OK);
        assertThat(requestHandler2.getController()).hasFieldOrPropertyWithValue("method", HttpMethod.GET);
        assertThat(requestHandler2.getController()).hasFieldOrPropertyWithValue("route", "/api/talks/" + talkId);
    }

    @Test
    public void shouldInvokeUpdateMethod() throws InvocationTargetException, IllegalAccessException, IOException {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        int id = new Faker().number().numberBetween(0, 10000);
        String[] requestCases = {
                "GET /api/talks/" + id + " HTTP/1.1",
                "GET /api/talks/" + id + " HTTP/1.1"
        };

        RequestHandler requestHandler1 = new RequestHandler(requestCases[0], defaultHeaders(), "");
        requestHandler1.processRequest(new PrintWriter(stream));

        HttpParser httpParser = new HttpParser();
        HttpResponse response1 = httpParser.parseResponse(new ByteArrayInputStream(stream.toByteArray()));
        assertThat(response1).hasFieldOrPropertyWithValue("httpVersion", "HTTP/1.1");
        assertThat(response1).hasFieldOrPropertyWithValue("status", HttpStatus.UnprocessableEntity);
        assertThat(requestHandler1.getController()).hasFieldOrPropertyWithValue("method", HttpMethod.GET);
        assertThat(requestHandler1.getController()).hasFieldOrPropertyWithValue("id", String.valueOf(id));
        assertThat(requestHandler1.getController()).hasFieldOrPropertyWithValue("route", "/api/talks/" + String.valueOf(id));

        stream = new ByteArrayOutputStream();
        RequestHandler requestHandler2 = new RequestHandler(requestCases[1], defaultHeaders(), "");
        requestHandler2.processRequest(new PrintWriter(stream));
        HttpResponse response2 = httpParser.parseResponse(new ByteArrayInputStream(stream.toByteArray()));
        assertThat(response2).hasFieldOrPropertyWithValue("httpVersion", "HTTP/1.1");
        assertThat(response2).hasFieldOrPropertyWithValue("status", HttpStatus.OK);
        assertThat(requestHandler2.getController()).hasFieldOrPropertyWithValue("method", HttpMethod.GET);
        assertThat(requestHandler2.getController()).hasFieldOrPropertyWithValue("route", "/api/talks/" + String.valueOf(id));
    }

    private HashMap<String, String> defaultHeaders() {
        HashMap<String, String> headers = new HashMap<>();
        headers.put("Host:", "localhost:8080");
        headers.put("Content-Type:", "application/x-www-form-urlencoded");
        headers.put("Connection:", "close");
        return headers;
    }
}
