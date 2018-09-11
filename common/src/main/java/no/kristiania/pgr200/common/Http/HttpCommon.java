package no.kristiania.pgr200.common.Http;

import com.google.gson.Gson;
import com.google.gson.JsonElement;

import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

public abstract class HttpCommon {
    protected Map<String, String> headers;
    protected String httpVersion;
    protected String body;
    protected JsonElement json;

    public HttpCommon() {
        this(new HashMap<>(), "");
    }

    public HttpCommon(JsonElement body) {
        this(new HashMap<>(), body.toString());
    }

    public HttpCommon(String body) {
        this(new HashMap<>(), body);
    }

    public HttpCommon(Map<String, String> headers) {
        this(headers, "");
    }

    public HttpCommon(Map<String, String> headers, String body) {
        this.httpVersion = "HTTP/1.1";
        if (headers == null) {
            headers = new HashMap<>();
        }
        this.headers = headers;
        this.setBody(body);
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
                this.json = (new Gson()).fromJson(body, JsonElement.class);
            }
        }
    }

    public JsonElement getJson() {
        return json;
    }

    public void writeToStream(OutputStream stream) throws IOException {
        stream.write(this.getFirstLine());
        byte[] body = this.body.getBytes();
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
