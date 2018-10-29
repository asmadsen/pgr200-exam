package no.kristiania.pgr200.common.Models;

import java.util.Objects;
import java.util.UUID;

public class Track extends BaseModel<Track>{

    protected UUID id;
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
