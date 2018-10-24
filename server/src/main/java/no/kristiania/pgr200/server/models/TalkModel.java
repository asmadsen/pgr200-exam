package no.kristiania.pgr200.server.models;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import no.kristiania.pgr200.common.Models.Talk;
import no.kristiania.pgr200.orm.BaseRecord;

import java.util.UUID;

public class TalkModel extends BaseRecord<Talk> {

    public TalkModel(){
        setState(new Talk());
    }

    public TalkModel(JsonElement jsonElement){
        JsonObject talk = jsonElement.getAsJsonObject();
        setState(new Talk(talk.get("title").toString(), talk.get("description").toString()));
    }

    public TalkModel(String title, String description){
        setState(new Talk(title, description));
    }

    public TalkModel(String id, String title, String description){
        setState(new Talk(UUID.fromString(id), title, description));
    }

    @Override
    public String getTable() {
        return "talks";
    }
}
