package no.kristiania.pgr200.server.models;

import com.google.gson.JsonObject;
import no.kristiania.pgr200.common.models.Talk;
import no.kristiania.pgr200.common.models.Topic;
import no.kristiania.pgr200.orm.annotations.Relation;
import no.kristiania.pgr200.orm.BaseRecord;
import no.kristiania.pgr200.orm.relations.HasMany;

import java.util.UUID;

public class TopicModel extends BaseRecord<TopicModel, Topic> {

    public TopicModel(){
        super(new Topic());
    }

    public TopicModel(UUID uuid, JsonObject topic) {
        super(new Topic(uuid, topic));
    }

    public TopicModel(JsonObject topic) {
        super(new Topic(topic));
    }

    public TopicModel(UUID uuid) {
        super(new Topic(uuid));
    }

    public TopicModel(String topic) {
        super(new Topic(topic));
    }

    @Override
    public String getTable() {
        return "topics";
    }

    @Relation
    public HasMany<TalkModel, Talk, TopicModel> talks() {
        return this.hasMany(new TalkModel(), "topic_id", "id");
    }
}
