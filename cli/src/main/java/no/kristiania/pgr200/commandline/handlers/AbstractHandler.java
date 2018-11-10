package no.kristiania.pgr200.commandline.handlers;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import no.kristiania.pgr200.common.http.HttpClient;
import no.kristiania.pgr200.common.http.HttpRequest;
import no.kristiania.pgr200.common.http.HttpResponse;
import org.slf4j.Logger;

import java.io.IOException;

import static no.kristiania.pgr200.common.http.HttpMethod.*;
import static org.slf4j.LoggerFactory.getLogger;

public abstract class AbstractHandler {
    private static Logger logger = getLogger(AbstractHandler.class);
    protected Gson gson = new GsonBuilder().setPrettyPrinting().serializeNulls().create();

    protected abstract String baseUrl();

    protected HttpResponse fetch(String baseUrl) {
        HttpClient httpClient = getHttpClient();
        HttpRequest httpRequest = new HttpRequest(GET, baseUrl);
        try {
            return httpClient.execute("localhost:8080", httpRequest);
        } catch (IOException e) {
            logger.error("fetch", e);
        }
        return null;
    }

    protected HttpResponse update(String baseUrl, JsonElement element) {
        HttpClient httpClient = getHttpClient();
        HttpRequest httpRequest = new HttpRequest(PUT,
                                         String.format("%s/%s", baseUrl, element.getAsJsonObject().get("id").getAsString()),
                                         element);
        try {
            return httpClient.execute("localhost:8080", httpRequest);
        } catch (IOException e) {
            logger.error("update", e);
        }
        return null;
    }

    protected HttpResponse insert(String baseUrl, JsonElement element) {
        HttpClient httpClient = getHttpClient();
        HttpRequest httpRequest = new HttpRequest(POST, baseUrl, element);
        try {
            return httpClient.execute("localhost:8080", httpRequest);
        } catch (IOException e) {
            logger.error("insert", e);
        }
        return null;
    }

    protected HttpResponse delete(String baseUrl, String id) {
        HttpClient httpClient = getHttpClient();
        HttpRequest httpRequest = new HttpRequest(DELETE, String.format("%s/%s", baseUrl, id));
        try {
            return httpClient.execute("localhost:8080", httpRequest);
        } catch (IOException e) {
            logger.error("delete", e);
        }
        return null;
    }

    protected HttpClient getHttpClient() {
        return new HttpClient();
    }

}
