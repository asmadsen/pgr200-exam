package no.kristiania.pgr200.common.http;

import com.google.gson.JsonElement;

import java.util.Map;

public class HttpRequest extends HttpCommon {
    private String uri;
    private HttpMethod httpMethod;

    public HttpRequest() {
    }

    public HttpRequest(HttpMethod method) {
        this(method, null, null, "");
    }

    public HttpRequest(HttpMethod method, String uri) {
        this(method, uri, null, "");
    }

    public HttpRequest(HttpMethod method, String uri, Map<String, String> headers) {
        this(method, uri, headers, "");
    }

    public HttpRequest(HttpMethod method, String uri, String body) {
        this(method, uri, null, body);
    }

    public HttpRequest(HttpMethod method, String uri, Map<String, String> headers, String body) {
        super(headers, body);
        this.httpMethod = method;
        this.uri = uri;
    }

    public HttpRequest(HttpMethod method, String uri, JsonElement body) {
        this(method, uri, null, body);
    }

    public HttpRequest(HttpMethod method, String uri, Map<String, String> headers, JsonElement body) {
        super(headers, body);
        this.httpMethod = method;
        this.uri = uri;
    }

    @Override
    protected byte[] getFirstLine() {
        return String.format("%s %s %s\r\n", this.httpMethod.name(), this.uri, this.httpVersion).getBytes();
    }

    public HttpMethod getHttpMethod() {
        return httpMethod;
    }

    public void setHttpMethod(HttpMethod httpMethod) {
        this.httpMethod = httpMethod;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }
}
