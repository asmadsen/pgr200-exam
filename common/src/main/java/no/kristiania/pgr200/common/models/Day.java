package no.kristiania.pgr200.common.models;

import com.google.gson.JsonObject;

import java.sql.Date;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Objects;
import java.util.UUID;

public class Day extends BaseModel<Day> {

    protected UUID id;
    public Date date;
    public UUID conference_id;

    public Day() {
    }

    public Day(UUID uuid) {
        setId(uuid);
    }

    public Day(String date) {
        setDate(date);
    }

    public Day(UUID uuid, JsonObject jsonObject) {
        setId(uuid);
        if(jsonObject.get("date") != null) setDate(jsonObject.get("date").getAsString());
        if(jsonObject.get("conference_id") != null)
            setConference_id(UUID.fromString(jsonObject.get("conference_id").getAsString()));
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = Date.valueOf(LocalDate.parse(date, DateTimeFormatter.ISO_LOCAL_DATE));
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
