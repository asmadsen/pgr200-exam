package no.kristiania.pgr200.common.models;

import com.google.gson.JsonObject;

import javax.validation.constraints.NotBlank;
import java.util.Objects;
import java.util.UUID;

public class Talk extends BaseModel<Talk> {

    @no.kristiania.pgr200.common.annotations.UUID
    protected UUID id;
    @NotBlank
    public String title;
    @NotBlank
    public String description;
    @no.kristiania.pgr200.common.annotations.UUID
    public UUID topic_id;

    public Talk() {
    }

    public Talk(String title, String description) {
        setTitle(title);
        setDescription(description);
    }

    public Talk(UUID uuid) {
        setId(uuid);
    }

    public Talk(JsonObject talk) {
        if (talk.get("id") != null && !talk.get("id").isJsonNull())
            setId(UUID.fromString(talk.get("id").getAsString()));
        if (talk.get("title") != null && !talk.get("title").isJsonNull())
            setTitle(talk.get("title").getAsString());
        if (talk.get("description") != null && !talk.get("description").isJsonNull())
            setDescription(talk.get("description").getAsString());
        if (talk.get("topic_id") != null && !talk.get("topic_id").isJsonNull())
            setTopic_id(UUID.fromString(talk.get("topic_id").getAsString()));
    }

    public Talk(UUID uuid, JsonObject talk) {
        this(talk);
        setId(uuid);
    }

    public Talk(String title, String description, UUID topic_id) {
        setTitle(title);
        setDescription(description);
        setTopic_id(topic_id);
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public UUID getTopic_id() {
        return topic_id;
    }

    public void setTopic_id(UUID topic_id) {
        this.topic_id = topic_id;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Talk) {
            return Objects.equals(this.getId(), ((Talk) obj).getId()) &&
                    Objects.equals(this.getTitle(), ((Talk) obj).getTitle()) &&
                    Objects.equals(this.getDescription(), ((Talk) obj).getDescription()) &&
                    Objects.equals(this.getTopic_id(), ((Talk) obj).getTopic_id());
        }
        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, description);
    }
}
