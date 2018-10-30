package no.kristiania.pgr200.common.models;

import java.util.Objects;
import java.util.UUID;

public class Conference extends BaseModel<Conference> {

    protected UUID id;
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
