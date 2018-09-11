package no.kristiania.pgr200.common.Http;

public class HttpResponse extends HttpCommon {
    private HttpStatus status;
    private String httpVersion;

    public HttpStatus getStatus() {
        return status;
    }

    public void setStatus(HttpStatus status) {
        this.status = status;
    }

    public String getHttpVersion() {
        return httpVersion;
    }

    public void setHttpVersion(String httpVersion) {
        this.httpVersion = httpVersion;
    }

    @Override
    protected byte[] getFirstLine() {
        return new byte[0];
    }
}
