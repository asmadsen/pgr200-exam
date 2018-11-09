package no.kristiania.pgr200.common.models;

import com.google.gson.JsonObject;

import java.util.Objects;
import java.util.UUID;

public class Track extends BaseModel<Track> {

    @no.kristiania.pgr200.common.annotations.UUID
    protected UUID id;
    @no.kristiania.pgr200.common.annotations.UUID
    private UUID day_id;

    public Track() {

    }

    public Track(UUID uuid) {
        setId(uuid);
    }

    public Track(UUID id, UUID day_id) {
        setId(id);
        setDay_id(day_id);
    }

    public Track(JsonObject track) {
        if(track.get("id") != null && !track.get("id").isJsonNull()) {
            this.id = UUID.fromString(track.get("id").getAsString());
        }
        if(track.get("day_id") != null && !track.get("day_id").isJsonNull()) {
            this.day_id = UUID.fromString(track.get("day_id").getAsString());
        }
    }

    public Track(UUID id, JsonObject track) {
        this(track);
        setId(id);
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public void setDay_id(UUID day_id) {
        this.day_id = day_id;
    }

    public UUID getDay_id() {
        return day_id;
    }

    @Override
    public boolean equals(Object other) {
        if (other instanceof Track) {
            return this.getId().equals(((Track) other).getId()) &&
                    this.getDay_id().equals(((Track) other).getDay_id());
        }
        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, day_id);
    }
}
