package no.kristiania.pgr200.server.models;

import com.google.gson.JsonElement;
import no.kristiania.pgr200.server.annotations.Record;

import java.sql.SQLException;
import java.util.List;

public class Talk extends BaseModel<Talk> {
    private static final String TABLE = "talks";

    private String id;
    private String title;
    private String description;
    private String topic;

    public Talk(){

    }

    public Talk(JsonElement jsonElement){
        this.title = jsonElement.getAsJsonObject().get("title").toString();
        this.description = jsonElement.getAsJsonObject().get("description").toString();
        this.topic = jsonElement.getAsJsonObject().get("topic").toString();
    }

    public Talk(String title, String description){
        this.title = title;
        this.description = description;
    }

    public Talk(String id, String title, String description){
        this.id = id;
        this.title = title;
        this.description = description;
    }

    public static List<Talk> all() throws SQLException, NoSuchFieldException, InstantiationException, IllegalAccessException {
        return all(Talk.class);
    }

    public static Talk findBy(int id) throws Exception {
        return findBy(id);
    }

    @Record(type = "GET", column = "id")
    public String getId() { return this.id; }

    @Record(type = "GET", column = "title")
    public String getTitle() {
        return this.title;
    }

    @Record(type = "GET", column = "description")
    public String getDescription() {
        return this.description;
    }

    @Record(type = "GET", column = "topic")
    public String getTopic() {
        return topic;
    }

    @Record(type = "SET", column = "id")
    public void setId(String id) {
        this.id = id;
    }

    @Record(type = "SET", column = "title")
    public void setTitle(String title) {
        this.title = title;
    }

    @Record(type = "SET", column = "description")
    public void setDescription(String description) {
        this.description = description;
    }

    @Record(type = "SET", column = "topic")
    public void setTopic(String topic) {
        this.topic = topic;
    }
}
