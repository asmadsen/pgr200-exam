package no.kristiania.pgr200.server.models;

import com.google.gson.JsonObject;
import no.kristiania.pgr200.common.Models.Day;
import no.kristiania.pgr200.orm.BaseRecord;

import java.text.ParseException;
import java.time.format.DateTimeParseException;
import java.util.UUID;

public class DayModel extends BaseRecord<DayModel, Day> {

    public DayModel() {
        super(new Day());
    }

    public DayModel(UUID uuid, JsonObject jsonObject) throws DateTimeParseException {
        super(new Day(uuid, jsonObject.get("date").getAsString()));
    }

    public DayModel(JsonObject jsonObject) throws DateTimeParseException {
        super(new Day(jsonObject.get("date").getAsString()));
    }

    public DayModel(UUID uuid) {
        super(new Day(uuid));
    }

    @Override
    public String getTable() {
        return "days";
    }
}
