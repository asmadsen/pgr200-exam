package no.kristiania.pgr200.server.models;

import com.google.gson.JsonObject;
import no.kristiania.pgr200.common.Models.Talk;
import no.kristiania.pgr200.orm.BaseRecord;

import java.util.UUID;

public class TalkModel extends BaseRecord<Talk> {

    public TalkModel(){
        super(new Talk());
    }

    public TalkModel(JsonObject talk){
        super(new Talk(talk.get("title").getAsString(), talk.get("description").getAsString()));
    }

    public TalkModel(String id, JsonObject talk){
        super(new Talk(UUID.fromString(id), talk.get("title").getAsString(), talk.get("description").getAsString()));
    }

    public TalkModel(String title, String description){
        super(new Talk(title, description));
    }

    public TalkModel(String id, String title, String description){
        super(new Talk(UUID.fromString(id), title, description));
    }

    @Override
    public String getTable() {
        return "talks";
    }
}
