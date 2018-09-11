package no.kristiania.pgr200.server;

public class HttpErrorCodes {

    private ErrorCode errorCode;

    public HttpErrorCodes(String code, String id){
        errorCode = new ErrorCode(code, id);
    }
}
