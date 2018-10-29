package no.kristiania.pgr200.server.models;

import com.google.gson.JsonObject;
import no.kristiania.pgr200.common.Models.Conference;
import no.kristiania.pgr200.orm.BaseRecord;

import java.util.UUID;

public class ConferenceModel extends BaseRecord<Conference> {

    public ConferenceModel(UUID uuid) {
        super(new Conference(uuid));
    }

    public ConferenceModel() {
        super(new Conference());
    }

    public ConferenceModel(UUID uuid, JsonObject jsonObject) {
        super(new Conference(uuid, jsonObject.get("name").getAsString()));
    }

    public ConferenceModel(JsonObject jsonObject) {
        super(new Conference(jsonObject.get("name").getAsString()));
    }

    @Override
    public String getTable() {
        return "conferences";
    }
}
