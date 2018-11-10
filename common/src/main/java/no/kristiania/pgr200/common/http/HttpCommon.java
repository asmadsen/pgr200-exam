package no.kristiania.pgr200.common.http;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public abstract class HttpCommon {
    protected Map<String, String> headers;
    protected String httpVersion;
    protected String body;
    protected JsonElement json;

    public HttpCommon() {
        this(null, "");
    }

    public HttpCommon(Map<String, String> headers, String body) {
        this.httpVersion = "HTTP/1.1";
        if (headers == null) {
            headers = new HashMap<>();
        }
        this.headers = headers;
        this.setBody(body);
    }

    public HttpCommon(Map<String, String> headers, JsonElement body) {
        this.httpVersion = "HTTP/1.1";
        if (headers == null) {
            headers = new HashMap<>();
        }
        this.headers = headers;
        this.setBody(body);
    }

    private void setBody(JsonElement body) {
        if (this.headers != null) {
            this.headers.put("Content-Type", "application/json");
        }
        this.body = body.toString();
        this.json = body;
    }

    public Map<String, String> getHeaders() {
        return headers;
    }

    public void setHeaders(Map<String, String> headers) {
        this.headers = headers;
    }

    public String getHttpVersion() {
        return httpVersion;
    }

    public void setHttpVersion(String httpVersion) {
        this.httpVersion = httpVersion;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
        if (this.headers.containsKey("Content-Type")) {
            if (this.headers.get("Content-Type").equals("application/json")) {
                try {
                    this.json = (new Gson()).fromJson(body, JsonElement.class);
                    this.headers.put("Content-Length", String.valueOf(body.length()));
                } catch (JsonSyntaxException jsonSyntaxException) {
                    JsonObject error = new JsonObject();
                    JsonObject malformed = new JsonObject();
                    malformed.addProperty("message", jsonSyntaxException.getMessage());
                    error.add("error", malformed);
                    this.json = error;
                    this.body = error.toString();
                }
            }
        }
    }

    public JsonElement getJson() {
        return json;
    }

    public void writeToStream(OutputStream stream) throws IOException {
        stream.write(this.getFirstLine());
        byte[] body = this.body.getBytes(StandardCharsets.UTF_8);
        if (body.length > 0) {
            this.headers.put("Content-Length", String.valueOf(body.length));
        }
        for (Map.Entry<String, String> header : headers.entrySet()) {
            stream.write(String.format("%s: %s\r\n", HttpUtils.capitalizeHeaderKey(header.getKey()), header.getValue())
                               .getBytes());
        }
        stream.write("\r\n".getBytes());
        if (body.length > 0) {
            stream.write(body);
        }
    }

    protected abstract byte[] getFirstLine();
}
