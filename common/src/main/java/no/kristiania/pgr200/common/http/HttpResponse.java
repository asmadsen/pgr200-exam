package no.kristiania.pgr200.common.http;

import com.google.gson.JsonElement;

import java.util.HashMap;
import java.util.Map;

import static java.nio.charset.StandardCharsets.UTF_8;

public class HttpResponse extends HttpCommon {
    private HttpStatus status;

    public HttpResponse(HttpStatus status, String body) {
        this(status, null, body);
    }

    public HttpResponse(HttpStatus status, Map<String, String> headers, String body) {
        super(headers, body);
        this.status = status;
    }

    public HttpResponse() {
        super();
    }

    public HttpResponse(HttpStatus status) {
        this(status, null, "");
    }

    public HttpResponse(HttpStatus status, HashMap<String, String> headers) {
        this(status, headers, "");
    }

    public HttpResponse(HttpStatus status, JsonElement body) {
        this(status, null, body);
    }

    public HttpResponse(HttpStatus status, Map<String, String> headers, JsonElement body) {
        super(headers, body);
        this.status = status;
    }

    public HttpStatus getStatus() {
        return status;
    }

    public void setStatus(HttpStatus status) {
        this.status = status;
    }

    @Override
    protected byte[] getFirstLine() {
        return String.format("%s %s %s\r\n", this.httpVersion, this.status.getStatusCode(), this.status.realName())
                     .getBytes(UTF_8);
    }
}
