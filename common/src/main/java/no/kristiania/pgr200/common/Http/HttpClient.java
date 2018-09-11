package no.kristiania.pgr200.common.Http;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class HttpClient {

    private HttpParser httpParser = new HttpParser();

    public HttpResponse execute(String host, HttpRequest request) throws IOException {
        Pattern p = Pattern.compile("^(?:(\\w+)://)?((?:(?:[a-zA-Z0-9]|[a-zA-Z0-9][a-zA-Z0-9\\-]*[a-zA-Z0-9])\\.)*(?:[A-Za-z0-9]|[A-Za-z0-9][A-Za-z0-9\\-]*[A-Za-z0-9]))(?::(\\d+))?$");
        int port = 80;
        if (host != null) {
            List<String> list = new ArrayList<String>();
            Matcher m = p.matcher(host);
            while(m.find()) {
                if (m.group(1) != null) {
                    switch (m.group(1)) {
                        case "https":
                            port = 443;
                            break;
                    }
                }
                host = m.group(2);
                if (m.group(3) != null) {
                    port = Integer.parseInt(m.group(3));
                }
                break;
            }
        }
        return this.execute(host, port, request);
    }

    public HttpResponse execute(String host, int port, HttpRequest request) throws IOException {
        if (!this.validateRequest(request)) {
            throw new RuntimeException();
        }

        try (Socket socket = new Socket(host, port)) {
            Map<String, String> headers = request.getHeaders();
            if (!headers.containsKey("Accept")) {
                headers.put("Accept", "application/json");
            }
            if (!headers.containsKey("Connection")) {
                headers.put("Connection", "close");
            }
            if (!headers.containsKey("Host")) {
                headers.put("Host", String.format("%s:%d", host, port));
            }
            OutputStream outputStream = socket.getOutputStream();
            request.writeToStream(outputStream);
            outputStream.flush();

            HttpResponse response = this.httpParser.parseResponse(socket.getInputStream());
            return response;
        }
    }

    private boolean validateRequest(HttpRequest request) {
        if (request.getUri().isEmpty() || request.getUri().charAt(0) != '/') {
            return false;
        }
        if (request.getHttpMethod() == null) {
            return false;
        }
        if (request.getHttpVersion() == null) {
            return false;
        }
        return true;
    }
}
