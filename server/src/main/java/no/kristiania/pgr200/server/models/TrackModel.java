package no.kristiania.pgr200.server.models;

import com.google.gson.JsonObject;
import no.kristiania.pgr200.common.models.Timeslot;
import no.kristiania.pgr200.common.models.Topic;
import no.kristiania.pgr200.common.models.Track;
import no.kristiania.pgr200.orm.BaseRecord;
import no.kristiania.pgr200.orm.annotations.Relation;
import no.kristiania.pgr200.orm.relations.BelongsTo;
import no.kristiania.pgr200.orm.relations.HasMany;

import java.util.UUID;

public class TrackModel extends BaseRecord<TrackModel, Track> {

    public TrackModel() {
        super(new Track());
    }

    public TrackModel(UUID uuid) {
        super(new Track(uuid));
    }

    public TrackModel(JsonObject track) {
        super(new Track(track));
    }

    public TrackModel(UUID uuid, JsonObject jsonObject) {
        super(new Track(uuid, jsonObject));
    }

    public TrackModel(UUID id, UUID day_id) {
        super(new Track(id, day_id));
    }

    @Override
    public String getTable() {
        return "tracks";
    }

    @Relation
    public HasMany<TimeslotModel, Timeslot, TrackModel> timeslots() {
        return this.hasMany(new TimeslotModel(), "track_id", "id");
    }
}
