package no.kristiania.pgr200.server.models;

import com.google.gson.JsonObject;
import no.kristiania.pgr200.common.models.Talk;
import no.kristiania.pgr200.common.models.Topic;
import no.kristiania.pgr200.orm.annotations.Relation;
import no.kristiania.pgr200.orm.BaseRecord;
import no.kristiania.pgr200.orm.relations.BelongsTo;

import java.util.UUID;

public class TalkModel extends BaseRecord<TalkModel, Talk> {

    public TalkModel(){
        super(new Talk());
    }

    public TalkModel(JsonObject talk){
        super(new Talk(talk));
    }

    public TalkModel(UUID uuid, JsonObject talk){
        super(new Talk(uuid, talk));
    }

    public TalkModel(String title, String description){
        super(new Talk(title, description));
    }

    public TalkModel(UUID uuid) {
        super(new Talk(uuid));
    }

    public TalkModel(String title, String description, UUID topic_id) {
        super(new Talk(title, description, topic_id));
    }

    @Override
    public String getTable() {
        return "talks";
    }

    @Relation
    public BelongsTo<TopicModel, Topic, TalkModel> topic() {
        return this.belongsTo(new TopicModel(), "topic_id", "id");
    }
}
