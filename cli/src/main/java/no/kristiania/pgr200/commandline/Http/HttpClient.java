package no.kristiania.pgr200.commandline.Http;

import com.google.gson.Gson;
import com.google.gson.JsonElement;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class HttpClient {

    private InetAddress address;
    private int port;
    private Gson gson = new Gson();

    public HttpClient(String hostName) {
        try {
            String host = hostName.split(":")[0];
            this.port = Integer.parseInt(hostName.split(":")[1]);
            this.address = InetAddress.getByName(host);
        } catch (UnknownHostException e) {
        }
    }

    public HttpClient() {
    }

    public <T> Response<T> get(String path, Map<String, String> headers, Class<T> responseClass) {
        return this.request(Method.GET, path, headers, "", responseClass);
    }

    public <T> Response<T> get(String path, Class<T> responseClass) {
        return this.get(path, null, responseClass);
    }

    public Response<JsonElement> get(String path, Map<String, String> headers) {
        return this.get(path, headers, JsonElement.class);
    }

    public Response<JsonElement> get(String path) {
        return this.get(path, null, JsonElement.class);
    }

    public <T> Response<T> delete(String path, Map<String, String> headers, Class<T> responseClass) {
        return this.request(Method.DELETE, path, headers, "", responseClass);
    }

    public <T> Response<T> delete(String path, Class<T> responseClass) {
        return this.delete(path, null, responseClass);
    }

    public Response<JsonElement> delete(String path, Map<String, String> headers) {
        return this.delete(path, headers, JsonElement.class);
    }

    public Response<JsonElement> delete(String path) {
        return this.delete(path, null, JsonElement.class);
    }

    public <T> Response<T> post(String path, JsonElement jsonData, Map<String, String> headers, Class<T> responseClass) {
        headers.put("Content-Type", "application/json");
        return this.request(Method.POST, path, headers, jsonData.toString(), responseClass);
    }

    public <T> Response<T> post(String path, JsonElement jsonData, Class<T> responseClass) {
        return this.post(path, jsonData, new HashMap<>(), responseClass);
    }

    public Response<JsonElement> post(String path, JsonElement jsonData, Map<String, String> headers) {
        return this.post(path, jsonData, headers, JsonElement.class);
    }

    public Response<JsonElement> post(String path, JsonElement jsonData) {
        return this.post(path, jsonData, new HashMap<>(), JsonElement.class);
    }

    public <T> Response<T> post(String path, FormData formData, Map<String, String> headers, Class<T> responseClass) {
        headers.put("Content-Type", "application/x-www-form-urlencoded; charset=utf-8");
        return this.request(Method.POST, path, headers, formData.toString(), responseClass);
    }

    public <T> Response<T> post(String path, FormData formData, Class<T> responseClass) {
        return this.post(path, formData, new HashMap<>(), responseClass);
    }

    public Response<JsonElement> post(String path, FormData formData, Map<String, String> headers) {
        return this.post(path, formData, headers, JsonElement.class);
    }

    public Response<JsonElement> post(String path, FormData formData) {
        return this.post(path, formData, new HashMap<>(), JsonElement.class);
    }

    public <T> Response<T> put(String path, JsonElement jsonData, Map<String, String> headers, Class<T> responseClass) {
        headers.put("Content-Type", "application/json");
        return this.request(Method.PUT, path, headers, jsonData.toString(), responseClass);
    }

    public <T> Response<T> put(String path, JsonElement jsonData, Class<T> responseClass) {
        return this.put(path, jsonData, new HashMap<>(), responseClass);
    }

    public Response<JsonElement> put(String path, JsonElement jsonData, Map<String, String> headers) {
        return this.put(path, jsonData, headers, JsonElement.class);
    }

    public Response<JsonElement> put(String path, JsonElement jsonData) {
        return this.put(path, jsonData, new HashMap<>(), JsonElement.class);
    }

    public <T> Response<T> put(String path, FormData formData, Map<String, String> headers, Class<T> responseClass) {
        headers.put("Content-Type", "application/x-www-form-urlencoded; charset=utf-8");
        return this.request(Method.PUT, path, headers, formData.toString(), responseClass);
    }

    public <T> Response<T> put(String path, FormData formData, Class<T> responseClass) {
        return this.put(path, formData, new HashMap<>(), responseClass);
    }

    public Response<JsonElement> put(String path, FormData formData, Map<String, String> headers) {
        return this.put(path, formData, headers, JsonElement.class);
    }

    public Response<JsonElement> put(String path, FormData formData) {
        return this.put(path, formData, new HashMap<>(), JsonElement.class);
    }

    private <T> Response<T> request(Method method, String path, Map<String, String> headers, String body, Class<T> responseClass) {
        Response<T> response = new Response<>();
        if (headers == null) {
            headers = new HashMap<>();
        }
        if (!headers.containsKey("Accept")) {
            headers.put("Accept", "application/json");
        }
        if (!headers.containsKey("Connection")) {
            headers.put("Connection", "close");
        }
        if (!headers.containsKey("Host")) {
            headers.put("Host", String.format("%s:%d", this.address.getHostName(), this.port));
        }
        if (!headers.containsKey("Content-Length") || !headers.get("Content-Length").equals(body)) {
            headers.put("Content-Length", String.valueOf(body.length()));
        }
        try {
            Socket socket = new Socket(this.address, this.port);
            BufferedWriter wr = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream(),
                                                                          StandardCharsets.UTF_8));
            wr.write(String.format("%s %s HTTP/1.1\r\n", method, path));
            for (Map.Entry<String, String> entry : headers.entrySet()) {
                wr.write(String.format("%s: %s\r\n", entry.getKey(), entry.getValue()));
            }
            wr.write("\r\n");
            // Send parameters
            wr.write(body);
            wr.flush();


            BufferedReader rd = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            String line;
            boolean isFirstLine = true;
            boolean headerIsDone = false;
            Map<String, String> responseHeaders = new HashMap<>();
            StringBuilder responseBody = new StringBuilder();

            while ((line = rd.readLine()) != null) {
                if (isFirstLine) {
                    String[] firstLine = line.split(" ");
                    response.setHttpVersion(firstLine[0]);
                    response.setStatusCode(Integer.parseInt(firstLine[1]));
                    response.setStatus(firstLine[2]);
                    isFirstLine = false;
                } else if (!headerIsDone) {
                    if (line.trim().isEmpty()) {
                        headerIsDone = true;
                        response.setHeaders(responseHeaders);
                    } else {
                        String[] headerLine = line.split(":");
                        responseHeaders.put(headerLine[0].trim(), headerLine[1].trim());
                    }
                } else {
                    responseBody.append(line);
                }
            }

            response.setBody(gson.fromJson(responseBody.toString(), responseClass));

            wr.close();
            rd.close();
            socket.close();

        } catch (IOException e) {
        }
        return response;
    }
}
