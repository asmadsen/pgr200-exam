package no.kristiania.pgr200.common.Models;

import java.util.Objects;
import java.util.UUID;

public class Topic extends BaseModel<Topic>{

    protected UUID id;
    private String topic;

    public Topic() {
    }

    public Topic(String topic) {
        setTopic(topic);
    }

    public Topic(UUID id, String topic) {
        this.id = id;
        this.topic = topic;
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

    private void setTopic(String topic) {
        this.topic = topic;
    }

    @Override
    public boolean equals(Object other) {
        if (other instanceof Topic) {
            return this.getId().equals(((Topic) other).getId()) && this.getTopic().equals(((Topic) other).getTopic());
        }
        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, topic);
    }
}
