package no.kristiania.pgr200.server.models;

import com.google.gson.JsonObject;
import no.kristiania.pgr200.common.models.Timeslot;
import no.kristiania.pgr200.orm.BaseRecord;

import java.util.UUID;

public class TimeslotModel extends BaseRecord<TimeslotModel, Timeslot> {
    public TimeslotModel() {
        super(new Timeslot());
    }

    public TimeslotModel(UUID uuid) {
        super(new Timeslot(uuid));
    }

    public TimeslotModel(UUID uuid, JsonObject jsonObject) {
        super(new Timeslot(uuid,
                UUID.fromString(jsonObject.get("talk_id").getAsString()),
                UUID.fromString(jsonObject.get("track_id").getAsString())));
    }

    public TimeslotModel(JsonObject jsonObject) {
        super(new Timeslot(UUID.fromString(jsonObject.get("talk_id").getAsString()),
                UUID.fromString(jsonObject.get("track_id").getAsString())));
    }

    @Override
    public String getTable() {
        return "timeslots";
    }
}
