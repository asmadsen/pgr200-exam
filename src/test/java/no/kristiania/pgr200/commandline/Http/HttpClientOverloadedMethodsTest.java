package no.kristiania.pgr200.commandline.Http;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.HashMap;

import static org.mockito.ArgumentMatchers.anyMap;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.isNull;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.*;

public class HttpClientOverloadedMethodsTest {

    private HttpClient client = mock(HttpClient.class);

    @Test
    public void shouldCallMainGetMethod() {
        when(client.get(anyString())).thenCallRealMethod();
        client.get("/onlyPath");
        verify(client).get("/onlyPath", null, JsonElement.class);

        when(client.get(anyString(), anyMap())).thenCallRealMethod();
        client.get("/withHeaders", new HashMap<>());
        verify(client).get(eq("/withHeaders"), anyMap(), eq(JsonElement.class));


        when(client.get(anyString(), Mockito.<Class<Object>>any())).thenCallRealMethod();
        client.get("/withExplicitType", JsonObject.class);
        verify(client).get(eq("/withExplicitType"), isNull(), eq(JsonObject.class));
    }

    @Test
    public void shouldCallMainFormDataPostMethod() {
        when(client.post(anyString(), any(FormData.class))).thenCallRealMethod();
        client.post("/onlyPath", new FormData());
        verify(client).post(eq("/onlyPath"), any(FormData.class), anyMap(), eq(JsonElement.class));

        when(client.post(anyString(), any(FormData.class), anyMap())).thenCallRealMethod();
        client.post("/withHeaders", new FormData(), new HashMap<>());
        verify(client).post(eq("/withHeaders"), any(FormData.class), anyMap(), eq(JsonElement.class));

        when(client.post(anyString(), any(FormData.class), Mockito.<Class<Object>>any())).thenCallRealMethod();
        client.post("/withExplicitType", new FormData(), JsonObject.class);
        verify(client).post(eq("/withExplicitType"), any(FormData.class), anyMap(), eq(JsonObject.class));
    }

    @Test
    public void shouldCallMainJsonDataPostMethod() {
        when(client.post(anyString(), any(JsonElement.class))).thenCallRealMethod();
        client.post("/onlyPath", new JsonObject());
        verify(client).post(eq("/onlyPath"), any(JsonElement.class), anyMap(), eq(JsonElement.class));

        when(client.post(anyString(), any(JsonElement.class), anyMap())).thenCallRealMethod();
        client.post("/withHeaders", new JsonObject(), new HashMap<>());
        verify(client).post(eq("/withHeaders"), any(JsonElement.class), anyMap(), eq(JsonElement.class));

        when(client.post(anyString(), any(JsonElement.class), Mockito.<Class<Object>>any())).thenCallRealMethod();
        client.post("/withExplicitType", new JsonObject(), JsonObject.class);
        verify(client).post(eq("/withExplicitType"), any(JsonElement.class), anyMap(), eq(JsonObject.class));
    }

    @Test
    public void shouldCallMainDeleteMethod() {
        when(client.delete(anyString())).thenCallRealMethod();
        client.delete("/onlyPath");
        verify(client).delete(eq("/onlyPath"), isNull(), eq(JsonElement.class));

        when(client.delete(anyString(), anyMap())).thenCallRealMethod();
        client.delete("/withHeaders", new HashMap<>());
        verify(client).delete(eq("/withHeaders"), anyMap(), eq(JsonElement.class));

        when(client.delete(anyString(), Mockito.<Class<Object>>any())).thenCallRealMethod();
        client.delete("/withExplicitType", JsonObject.class);
        verify(client).delete(eq("/withExplicitType"), isNull(), eq(JsonObject.class));
    }

    @Test
    public void shouldCallMainFormDataPutMethod() {
        when(client.put(anyString(), any(FormData.class))).thenCallRealMethod();
        client.put("/onlyPath", new FormData());
        verify(client).put(eq("/onlyPath"), any(FormData.class), anyMap(), eq(JsonElement.class));

        when(client.put(anyString(), any(FormData.class), anyMap())).thenCallRealMethod();
        client.put("/withHeaders", new FormData(), new HashMap<>());
        verify(client).put(eq("/withHeaders"), any(FormData.class), anyMap(), eq(JsonElement.class));

        when(client.put(anyString(), any(FormData.class), Mockito.<Class<Object>>any())).thenCallRealMethod();
        client.put("/withExplicitType", new FormData(), JsonObject.class);
        verify(client).put(eq("/withExplicitType"), any(FormData.class), anyMap(), eq(JsonObject.class));
    }

    @Test
    public void shouldCallMainJsonDataPutMethod() {
        when(client.put(anyString(), any(JsonElement.class))).thenCallRealMethod();
        client.put("/onlyPath", new JsonObject());
        verify(client).put(eq("/onlyPath"), any(JsonElement.class), anyMap(), eq(JsonElement.class));

        when(client.put(anyString(), any(JsonElement.class), anyMap())).thenCallRealMethod();
        client.put("/withHeaders", new JsonObject(), new HashMap<>());
        verify(client).put(eq("/withHeaders"), any(JsonElement.class), anyMap(), eq(JsonElement.class));

        when(client.put(anyString(), any(JsonElement.class), Mockito.<Class<Object>>any())).thenCallRealMethod();
        client.put("/withExplicitType", new JsonObject(), JsonObject.class);
        verify(client).put(eq("/withExplicitType"), any(JsonElement.class), anyMap(), eq(JsonObject.class));
    }
}
