package no.kristiania.pgr200.common.Models;

import java.sql.Date;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Objects;
import java.util.UUID;

public class Day extends BaseModel<Day> {

    protected UUID id;
    private Date date;

    public Day() {
    }

    public Day(UUID uuid, String date) {
        setId(uuid);
        setDate(Date.valueOf(LocalDate.parse(date, DateTimeFormatter.ISO_LOCAL_DATE)));
    }

    public Day(String date) {
        this(null, date);
    }

    public Day(UUID uuid) {
        setId(uuid);
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

    public void setDate(Date date) {
        this.date = date;
    }

    @Override
    public boolean equals(Object other) {
        if (other instanceof Day) {
            return this.getId().equals(((Day) other).getId()) && this.getDate().equals(((Day) other).getDate());
        }
        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, date);
    }
}
