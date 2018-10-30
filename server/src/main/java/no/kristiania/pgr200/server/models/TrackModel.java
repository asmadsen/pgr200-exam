package no.kristiania.pgr200.server.models;

import com.google.gson.JsonObject;
import no.kristiania.pgr200.common.Models.Track;
import no.kristiania.pgr200.orm.BaseRecord;

import java.util.UUID;

public class TrackModel extends BaseRecord<TrackModel, Track> {

    public TrackModel() {
        super(new Track());
    }

    public TrackModel(UUID uuid) {
        super(new Track(uuid));
    }

    public TrackModel(JsonObject jsonObject) {
        super(new Track(null, UUID.fromString(jsonObject.get("day_id").getAsString())));
    }

    public TrackModel(UUID uuid, JsonObject jsonObject) {
        super(new Track(uuid, UUID.fromString(jsonObject.get("day_id").getAsString())));
    }

    @Override
    public String getTable() {
        return "tracks";
    }
}
