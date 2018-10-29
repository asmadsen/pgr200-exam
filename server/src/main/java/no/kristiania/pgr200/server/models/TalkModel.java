package no.kristiania.pgr200.server.models;

import com.google.gson.JsonObject;
import no.kristiania.pgr200.common.Models.Talk;
import no.kristiania.pgr200.orm.BaseRecord;

import java.util.UUID;

public class TalkModel extends BaseRecord<TalkModel, Talk> {

    public TalkModel(){
        super(new Talk());
    }

    public TalkModel(JsonObject talk){
        super(new Talk(talk));
    }

    public TalkModel(UUID uuid, JsonObject talk){
        super(new Talk(uuid, talk));
    }

    public TalkModel(String title, String description){
        super(new Talk(title, description));
    }

    public TalkModel(UUID uuid) {
        super(new Talk(uuid));
    }

    @Override
    public String getTable() {
        return "talks";
    }
}
