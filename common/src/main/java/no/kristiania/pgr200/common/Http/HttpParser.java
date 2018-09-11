package no.kristiania.pgr200.common.Http;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class HttpParser {

    public HttpRequest parseRequest(InputStream stream) throws IOException {
        HttpRequest request = new HttpRequest();

        String line = readNextLine(stream);
        if (!line.trim().isEmpty()) {
            String[] parts = line.trim().split(" ", 3);
            request.setHttpMethod(HttpMethod.valueOf(parts[0]));
            request.setUri(parts[1]);
            request.setHttpVersion(parts[2]);
        }

        request.setHeaders(parseHeaders(stream));

        request.setBody(parseBody(stream));

        return request;
    }

    public HttpResponse parseResponse(InputStream stream) throws IOException {
        HttpResponse response = new HttpResponse();

        String line = readNextLine(stream);
        if (!line.trim().isEmpty()) {
            String[] parts = line.trim().split(" ", 3);
            response.setHttpVersion(parts[0]);
            response.setStatus(HttpStatus.valueOf(Integer.parseInt(parts[1])));
        }

        response.setHeaders(parseHeaders(stream));

        response.setBody(parseBody(stream));

        return response;
    }

    private String parseBody(InputStream stream) throws IOException {
        StringBuilder builder = new StringBuilder();
        int c;
        while ((c = stream.read()) != -1) {
            builder.append((char) c);
        }
        return builder.toString();
    }

    private Map<String, String> parseHeaders(InputStream stream) throws IOException {
        Map<String, String> headers = new HashMap<>();
        String line;
        while (!(line = readNextLine(stream)).trim().isEmpty()) {
            String[] parts = line.trim().split(": ");
            headers.put(HttpUtils.capitalizeHeaderKey(parts[0].trim()), parts[1].trim());
        }
        return headers;
    }

    private String readNextLine(InputStream stream) throws IOException {
        StringBuilder builder = new StringBuilder();
        int c;
        while ((c = stream.read()) != -1) {
            builder.append((char) c);
            if (c == '\n') break;
        }
        return builder.toString();
    }
}
