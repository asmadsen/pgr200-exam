package no.kristiania.pgr200.common.http;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

import static java.nio.charset.StandardCharsets.UTF_8;

public class HttpParser {

    public HttpRequest parseRequest(InputStream stream) throws IOException {
        InputStreamReader reader = new InputStreamReader(stream, UTF_8);
        HttpRequest request = new HttpRequest();

        String line = readNextLine(reader);
        if (!line.trim().isEmpty()) {
            String[] parts = line.trim().split(" ", 3);
            request.setHttpMethod(HttpMethod.valueOf(parts[0]));
            request.setUri(parts[1]);
            request.setHttpVersion(parts[2]);
        }

        Map<String, String> headers = parseHeaders(reader);
        request.setHeaders(headers);

        if (headers.containsKey("Content-Length") || reader.ready()) {
            request.setBody(parseBody(reader, Integer.parseInt(headers.get("Content-Length"))));
        }

        return request;
    }

    public HttpResponse parseResponse(InputStream stream) throws IOException {
        InputStreamReader reader = new InputStreamReader(stream, UTF_8);
        HttpResponse response = new HttpResponse();

        String line = readNextLine(reader);
        if (!line.trim().isEmpty()) {
            String[] parts = line.trim().split(" ", 3);
            response.setHttpVersion(parts[0]);
            response.setStatus(HttpStatus.valueOf(Integer.parseInt(parts[1])));
        }

        Map<String, String> headers = parseHeaders(reader);
        response.setHeaders(headers);

        if (headers.containsKey("Content-Length") || reader.ready()) {
            response.setBody(parseBody(reader, Integer.parseInt(headers.get("Content-Length"))));
        }

        return response;
    }

    private String parseBody(InputStreamReader reader, int length) throws IOException {
        StringBuilder builder = new StringBuilder();
        if (length == 0) return builder.toString();
        int c;
        int read = 0;
        while ((c = reader.read()) != -1) {
            builder.append((char) c);
            read = read + String.valueOf((char) c).getBytes(UTF_8).length;
            if (read >= length) {
                break;
            }
        }
        return builder.toString();
    }

    private Map<String, String> parseHeaders(InputStreamReader reader) throws IOException {
        Map<String, String> headers = new HashMap<>();
        String line;
        while (!(line = readNextLine(reader)).trim().isEmpty()) {
            String[] parts = line.trim().split(": ");
            headers.put(HttpUtils.capitalizeHeaderKey(parts[0].trim()), parts[1].trim());
        }
        return headers;
    }

    private String readNextLine(InputStreamReader reader) throws IOException {
        StringBuilder builder = new StringBuilder();
        int c;
        while ((c = reader.read()) != -1) {
            builder.append((char) c);
            if (c == '\n') break;
        }
        return builder.toString();
    }
}
