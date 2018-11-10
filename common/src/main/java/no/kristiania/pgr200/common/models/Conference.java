package no.kristiania.pgr200.common.models;

import com.google.gson.JsonObject;

import javax.validation.constraints.NotBlank;
import java.util.Objects;
import java.util.UUID;

public class Conference extends BaseModel<Conference> {

    @no.kristiania.pgr200.common.annotations.UUID
    protected UUID id;
    @NotBlank
    private String name;

    public Conference() {

    }

    public Conference(UUID uuid, String name) {
        setId(uuid);
        setName(name);
    }

    public Conference(String name) {
        setName(name);
    }

    public Conference(UUID uuid) {
        setId(uuid);
    }

    public Conference(UUID uuid, JsonObject conference) {
        this(conference);
        setId(uuid);
    }

    public Conference(JsonObject conference) {
        if (conference.get("name") != null && !conference.get("name").isJsonNull())
            setName(conference.get("name").getAsString());
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object other) {
        if (other instanceof Conference) {
            return this.getId().equals(((Conference) other).getId()) &&
                    this.getName().equals(((Conference) other).getName());
        }
        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name);
    }
}
