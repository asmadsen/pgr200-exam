package no.kristiania.pgr200.server.models;

import com.google.gson.JsonObject;
import no.kristiania.pgr200.common.models.Conference;
import no.kristiania.pgr200.common.models.Day;
import no.kristiania.pgr200.orm.BaseRecord;
import no.kristiania.pgr200.orm.annotations.Relation;
import no.kristiania.pgr200.orm.relations.HasMany;

import java.util.UUID;

public class ConferenceModel extends BaseRecord<ConferenceModel, Conference> {

    public ConferenceModel(UUID uuid) {
        super(new Conference(uuid));
    }

    public ConferenceModel() {
        super(new Conference());
    }

    public ConferenceModel(UUID uuid, JsonObject jsonObject) {
        super(new Conference(uuid, jsonObject));
    }

    public ConferenceModel(JsonObject jsonObject) {
        super(new Conference(jsonObject));
    }

    public ConferenceModel(String name) {
        super(new Conference(name));
    }

    @Override
    public String getTable() {
        return "conferences";
    }

    @Relation
    public HasMany<DayModel, Day, ConferenceModel> days() {
        return this.hasMany(new DayModel(), "conference_id", "id");
    }
}
