package no.kristiania.pgr200.server;

import com.google.gson.Gson;

import java.io.PrintWriter;
import java.util.Date;

public class HeaderResponse {

    public void sendSuccessResponse(PrintWriter out, String contentType){
        out.println("HTTP/1.1 200 OK");
        out.println("Date: " + new Date());
        out.println("Content-Type: " + contentType);
        out.println();
        out.flush();
    }


    public void sendUnprocessableResponse(PrintWriter out, String message) {
        out.println("HTTP/1.1 422 Unprocessable entity");
        out.println("Content-Type: application/json");
        out.println("Date: " + new Date());
        out.println();
        out.println(message);
        out.flush();
    }

    public void badRequest(PrintWriter out) {
        out.println("HTTP/1.1 400 Bad request");
        out.println("Date: " + new Date());
        out.println("Content-Type: application/json");
        out.println();
        HttpErrorCodes httpErrorCodes = new HttpErrorCodes("400", null);
        out.println(new Gson().toJson(httpErrorCodes));
        out.flush();
    }
}
