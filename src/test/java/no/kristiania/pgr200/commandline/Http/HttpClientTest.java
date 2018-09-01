package no.kristiania.pgr200.commandline.Http;

import com.github.javafaker.Faker;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;


public class HttpClientTest {

    private HttpClient client = new HttpClient("localhost:8080");
    private Gson gson = new Gson();
    private Faker faker = new Faker();

    @Test
    public void shouldMakeGetRequestWithCorrectHeaders() {
        Map<String, String> headers = new HashMap<String, String>();
        Response<JsonObject> response = client.get("/get", headers, JsonObject.class);
        JsonObject expect = new JsonObject();
        expect.add("args", new JsonObject());
        expect.add("headers", gson.toJsonTree(ResponseTestUtils.TransformKeys(headers)));
        expect.addProperty("origin", "172.17.0.1");
        expect.addProperty("url", "http://localhost:8080/get");
        JsonObject object = response.getBody();
        assertThat(object.entrySet())
                .isEqualTo(expect.entrySet());
    }

    @Test
    public void shouldPostJsonData() {
        Map<String, String> headers = new HashMap<String, String>();
        JsonObject jsonData = new JsonObject();
        jsonData.addProperty("age", faker.number().numberBetween(20, 50));
        jsonData.addProperty("name", faker.name().fullName());
        Response<JsonObject> response = client.post("/post", jsonData, headers, JsonObject.class);
        JsonObject expect = new JsonObject();
        expect.add("args", new JsonObject());
        expect.addProperty("data", jsonData.toString());
        expect.add("headers", gson.toJsonTree(ResponseTestUtils.TransformKeys(headers)));
        expect.addProperty("origin", "172.17.0.1");
        expect.add("files", new JsonObject());
        expect.add("form", new JsonObject());
        expect.add("json", jsonData);
        expect.addProperty("url", "http://localhost:8080/post");
        JsonObject object = response.getBody();
        assertThat(object.entrySet())
                .isEqualTo(expect.entrySet());
    }

    @Test
    public void shouldPostFormData() {
        Map<String, String> headers = new HashMap<String, String>();
        FormData formData = new FormData();
        formData.put("name", faker.name().fullName());
        formData.put("age", faker.number().numberBetween(20, 50));
        Response<JsonObject> response = client.post("/post", formData, headers, JsonObject.class);
        JsonObject expect = new JsonObject();
        expect.add("args", new JsonObject());
        expect.addProperty("data", "");
        expect.add("headers", gson.toJsonTree(ResponseTestUtils.TransformKeys(headers)));
        expect.addProperty("origin", "172.17.0.1");
        expect.add("files", new JsonObject());
        expect.add("form", gson.toJsonTree(formData));
        expect.add("json", null);
        expect.addProperty("url", "http://localhost:8080/post");
        JsonObject object = response.getBody();
        assertThat(object.entrySet())
                .isEqualTo(expect.entrySet());
    }



    @Test
    public void shouldPutJsonData() {
        Map<String, String> headers = new HashMap<String, String>();
        JsonObject jsonData = new JsonObject();
        jsonData.addProperty("age", faker.number().numberBetween(20, 50));
        jsonData.addProperty("name", faker.name().fullName());
        Response<JsonObject> response = client.put("/put", jsonData, headers, JsonObject.class);
        JsonObject expect = new JsonObject();
        expect.add("args", new JsonObject());
        expect.addProperty("data", jsonData.toString());
        expect.add("headers", gson.toJsonTree(ResponseTestUtils.TransformKeys(headers)));
        expect.addProperty("origin", "172.17.0.1");
        expect.add("files", new JsonObject());
        expect.add("form", new JsonObject());
        expect.add("json", jsonData);
        expect.addProperty("url", "http://localhost:8080/put");
        JsonObject object = response.getBody();
        assertThat(object.entrySet())
                .isEqualTo(expect.entrySet());
    }

    @Test
    public void shouldPutFormData() {
        Map<String, String> headers = new HashMap<String, String>();
        FormData formData = new FormData();
        formData.put("name", faker.name().fullName());
        formData.put("age", faker.number().numberBetween(20, 50));
        Response<JsonObject> response = client.put("/put", formData, headers, JsonObject.class);
        JsonObject expect = new JsonObject();
        expect.add("args", new JsonObject());
        expect.addProperty("data", "");
        expect.add("headers", gson.toJsonTree(ResponseTestUtils.TransformKeys(headers)));
        expect.addProperty("origin", "172.17.0.1");
        expect.add("files", new JsonObject());
        expect.add("form", gson.toJsonTree(formData));
        expect.add("json", null);
        expect.addProperty("url", "http://localhost:8080/put");
        JsonObject object = response.getBody();
        assertThat(object.entrySet())
                .isEqualTo(expect.entrySet());
    }

    @Test
    public void shouldMakeDeleteRequest() {
        Map<String, String> headers = new HashMap<String, String>();
        Response<JsonObject> response = client.delete("/delete", headers, JsonObject.class);
        JsonObject expect = new JsonObject();
        expect.add("args", new JsonObject());
        expect.addProperty("data", "");
        expect.add("headers", gson.toJsonTree(ResponseTestUtils.TransformKeys(headers)));
        expect.addProperty("origin", "172.17.0.1");
        expect.add("files", new JsonObject());
        expect.add("form", new JsonObject());
        expect.add("json", null);
        expect.addProperty("url", "http://localhost:8080/delete");
        JsonObject object = response.getBody();
        assertThat(object.entrySet())
                .isEqualTo(expect.entrySet());
    }
}