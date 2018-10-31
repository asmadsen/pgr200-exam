package no.kristiania.pgr200.server.models;

import com.google.gson.JsonObject;
import no.kristiania.pgr200.common.models.Conference;
import no.kristiania.pgr200.common.models.Day;
import no.kristiania.pgr200.common.models.Topic;
import no.kristiania.pgr200.orm.BaseRecord;
import no.kristiania.pgr200.orm.annotations.Relation;
import no.kristiania.pgr200.orm.relations.BelongsTo;

import java.time.format.DateTimeParseException;
import java.util.UUID;

public class DayModel extends BaseRecord<DayModel, Day> {

    public DayModel() {
        super(new Day());
    }

    public DayModel(UUID uuid, JsonObject jsonObject) throws DateTimeParseException {
        super(new Day(uuid, jsonObject));
    }

    public DayModel(JsonObject jsonObject) throws DateTimeParseException {
        super(new Day(jsonObject.get("date").getAsString()));
    }

    public DayModel(UUID uuid) {
        super(new Day(uuid));
    }

    @Override
    public String getTable() {
        return "days";
    }

    @Relation
    public BelongsTo<ConferenceModel, Conference, DayModel> conference() {
        return this.belongsTo(new ConferenceModel(), "conference_id", "id");
    }
}
