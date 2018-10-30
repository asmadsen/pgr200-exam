package no.kristiania.pgr200.server.models;

import com.google.gson.JsonObject;
import no.kristiania.pgr200.common.Models.Topic;
import no.kristiania.pgr200.orm.BaseRecord;
import java.util.UUID;

public class TopicModel extends BaseRecord<TopicModel, Topic> {

    public TopicModel(){
        super(new Topic());
    }

    public TopicModel(UUID uuid, JsonObject asJsonObject) {
        super(new Topic(uuid, asJsonObject.get("topic").getAsString()));
    }

    public TopicModel(JsonObject topic) {
        super(new Topic(topic.get("topic").getAsString()));
    }

    public TopicModel(UUID uuid) {
        super(new Topic(uuid));
    }

    @Override
    public String getTable() {
        return "topics";
    }
}
