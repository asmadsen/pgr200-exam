package no.kristiania.pgr200.common.models;

import com.google.gson.JsonObject;
import no.kristiania.pgr200.common.annotations.DateFormat;

import java.sql.Date;
import java.util.Objects;
import java.util.UUID;

public class Day extends BaseModel<Day> {

    @no.kristiania.pgr200.common.annotations.UUID
    protected UUID id;
    @DateFormat
    public String date;
    @no.kristiania.pgr200.common.annotations.UUID
    public UUID conference_id;

    public Day() {
    }

    public Day(UUID uuid) {
        setId(uuid);
    }

    public Day(String date, UUID conference_id) {
        setDate(date);
        setConference_id(conference_id);
    }

    public Day(UUID uuid, JsonObject day) {
        this(day);
        setId(uuid);
    }

    public Day(JsonObject day) {
        if(day.get("date") != null && !day.get("date").isJsonNull()) {
            setDate(day.get("date").getAsString());
        }
        if(day.get("conference_id") != null && !day.get("conference_id").isJsonNull()) {
            setConference_id(UUID.fromString(day.get("conference_id").getAsString()));
        }
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public UUID getConference_id() {
        return conference_id;
    }

    public void setConference_id(UUID conference_id) {
        this.conference_id = conference_id;
    }

    @Override
    public boolean equals(Object other) {
        if (other instanceof Day) {
            return this.getId().equals(((Day) other).getId()) &&
                    this.getDate().equals(((Day) other).getDate()) &&
                    this.getConference_id().equals(((Day) other).getConference_id());
        }
        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, date, conference_id);
    }
}
