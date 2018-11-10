package no.kristiania.pgr200.server;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import jdk.nashorn.internal.ir.Terminal;
import no.kristiania.pgr200.common.http.HttpClient;
import no.kristiania.pgr200.common.http.HttpMethod;
import no.kristiania.pgr200.common.http.HttpRequest;
import no.kristiania.pgr200.common.http.HttpResponse;
import no.kristiania.pgr200.common.models.BaseModel;
import no.kristiania.pgr200.common.models.Talk;
import no.kristiania.pgr200.orm.Orm;
import no.kristiania.pgr200.server.models.ConferenceModel;
import no.kristiania.pgr200.server.models.DayModel;

import javax.smartcardio.TerminalFactory;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.UUID;
import java.util.function.Function;

public class Scratch {
    public Scratch() throws IOException {
        HttpResponse response = new HttpClient().execute("localhost:8080", new HttpRequest(HttpMethod.GET, "/api/talks"));
        JsonArray talks = response.getJson().getAsJsonObject().get("values").getAsJsonArray();
        for (JsonElement talk : talks) {
            Talk t = jsonToModel(talk.toString(), Talk.class);
            System.out.println(String.format("%32s%10s%16s", t.getTitle(), t.getDescription(), t.getTopic_id()));
        }
    }

    public static <T extends BaseModel> T jsonToModel(String json, Class<T> tClass){
        return new Gson().fromJson(json, tClass);
    }

    public static void main(String[] args) throws IOException {
        new Scratch();
    }

}
