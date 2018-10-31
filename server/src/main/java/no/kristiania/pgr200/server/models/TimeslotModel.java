package no.kristiania.pgr200.server.models;

import com.google.gson.JsonObject;
import no.kristiania.pgr200.common.models.Talk;
import no.kristiania.pgr200.common.models.Timeslot;
import no.kristiania.pgr200.common.models.Track;
import no.kristiania.pgr200.orm.BaseRecord;
import no.kristiania.pgr200.orm.annotations.Relation;
import no.kristiania.pgr200.orm.relations.BelongsTo;
import no.kristiania.pgr200.orm.relations.HasMany;
import no.kristiania.pgr200.orm.relations.HasOne;

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

    @Relation
    public BelongsTo<TrackModel, Track, TimeslotModel> track() {
        return this.belongsTo(new TrackModel(), "track_id", "id");
    }

    @Relation
    public HasOne<TalkModel, Talk, TimeslotModel> talk() {
        return this.hasOne(new TalkModel(), "id", "talk_id");
    }
}
