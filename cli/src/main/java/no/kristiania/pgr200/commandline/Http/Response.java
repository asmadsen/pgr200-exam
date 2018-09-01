package no.kristiania.pgr200.commandline.Http;

import java.util.Map;

public class Response<T> {
    private T body;
    private String httpVersion;
    private int statusCode;
    private String status;
    private Map<String, String> headers;

    public Response() {

    }

    public Response(T body) {
        this.body = body;
    }

    public T getBody() {
        return body;
    }

    public void setBody(T body) {
        this.body = body;
    }

    public Response<T> withBody(T body) {
        this.body = body;
        return this;
    }

    public String getHttpVersion() {
        return httpVersion;
    }

    public void setHttpVersion(String httpVersion) {
        this.httpVersion = httpVersion;
    }

    public Response<T> withHttpVersion(String httpVersion) {
        this.httpVersion = httpVersion;
        return this;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public Response<T> withStatusCode(int statusCode) {
        this.statusCode = statusCode;
        return this;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Response<T> withStatus(String status) {
        this.status = status;
        return this;
    }

    public Map<String, String> getHeaders() {
        return headers;
    }

    public void setHeaders(Map<String, String> headers) {
        this.headers = headers;
    }

    public Response<T> withHeaders(Map<String, String> headers) {
        this.headers = headers;
        return this;
    }
}
