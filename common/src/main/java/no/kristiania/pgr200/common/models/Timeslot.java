package no.kristiania.pgr200.common.models;

import com.google.gson.JsonObject;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.Objects;
import java.util.UUID;

public class Timeslot extends BaseModel<Timeslot> {

    @no.kristiania.pgr200.common.annotations.UUID
    protected UUID id;
    @no.kristiania.pgr200.common.annotations.UUID(allowNull = false)
    public UUID talk_id;
    @no.kristiania.pgr200.common.annotations.UUID(allowNull = false)
    public UUID track_id;
    @NotNull
    @Min(0)
    public int slot_index;

    public Timeslot() {

    }

    public Timeslot(UUID uuid) {
        setId(uuid);
    }

    public Timeslot(UUID uuid, JsonObject topic) {
        this(topic);
        setId(uuid);
    }

    public Timeslot(JsonObject timeSlot) {
        if (timeSlot.get("talk_id") != null) this.talk_id = UUID.fromString(timeSlot.get("talk_id").getAsString());
        if (timeSlot.get("track_id") != null) this.track_id = UUID.fromString(timeSlot.get("track_id").getAsString());
        if (timeSlot.get("slot_index") != null) this.slot_index = timeSlot.get("slot_index").getAsInt();
    }

    public Timeslot(UUID talk_id, UUID track_id, int slot_index) {
        setTalk_id(talk_id);
        setTrack_id(track_id);
        setSlot_index(slot_index);
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getTalk_id() {
        return talk_id;
    }

    public void setTalk_id(UUID talk_id) {
        this.talk_id = talk_id;
    }

    public UUID getTrack_id() {
        return track_id;
    }

    public void setTrack_id(UUID track_id) {
        this.track_id = track_id;
    }

    public void setSlot_index(int slot_index) {
        this.slot_index = slot_index;
    }

    public int getSlot_index() {
        return slot_index;
    }

    @Override
    public boolean equals(Object other) {
        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, talk_id, track_id);
    }
}
