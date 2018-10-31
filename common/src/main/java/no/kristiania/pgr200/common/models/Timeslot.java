package no.kristiania.pgr200.common.models;

import java.util.Objects;
import java.util.UUID;

public class Timeslot extends BaseModel<Timeslot> {

    protected UUID id;
    public UUID talk_id;
    public UUID track_id;

    public Timeslot() {

    }

    public Timeslot(UUID uuid) {
        setId(uuid);
    }

    public Timeslot(UUID uuid, UUID talk_id, UUID track_id) {
        setId(uuid);
        setTalk_id(talk_id);
        setTrack_id(track_id);
    }

    public Timeslot(UUID talk_id, UUID track_id) {
        setTalk_id(talk_id);
        setTrack_id(track_id);
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

    @Override
    public boolean equals(Object other) {
        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, talk_id, track_id);
    }
}
