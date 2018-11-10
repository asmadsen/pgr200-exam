package no.kristiania.pgr200.common.models;

import com.google.gson.JsonObject;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Objects;
import java.util.UUID;

public class Topic extends BaseModel<Topic> {

    @no.kristiania.pgr200.common.annotations.UUID
    protected UUID id;
    @NotBlank
    private String topic;

    public Topic() {
    }

    public Topic(String topic) {
        setTopic(topic);
    }

    public Topic(UUID id, JsonObject topic) {
        this(topic);
        this.id = id;
    }

    public Topic(JsonObject topic){
        if(topic.get("topic") != null && !topic.get("topic").isJsonNull()) {
            this.topic = topic.get("topic").getAsString();
        }
    }

    public Topic(UUID uuid) {
        setId(uuid);
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    @Override
    public boolean equals(Object other) {
        if (other instanceof Topic) {
            return Objects.equals(this.getId(), ((Topic) other).getId()) &&
                    Objects.equals(this.getTopic(), ((Topic) other).topic);
        }
        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, topic);
    }
}
