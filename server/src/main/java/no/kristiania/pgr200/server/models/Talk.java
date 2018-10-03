package no.kristiania.pgr200.server.models;

import com.google.gson.JsonElement;

import java.util.List;

public class Talk extends Records<Talk> {
    public static final String TABLE = "talks";

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

    @SuppressWarnings("unchecked")
    public List<Talk> all() {
        return query(TABLE, "id", "title", "description");
    }

    @SuppressWarnings("unchecked")
    public Talk findBy(String id) {
        List<Talk> talks = query(TABLE, "id", "title", "description");
        return talks.get(0);
    }

    public String getId() { return this.id; }

    public String getTitle() {
        return this.title;
    }

    public String getDescription() {
        return this.description;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }
}
