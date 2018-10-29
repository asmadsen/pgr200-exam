package no.kristiania.pgr200.server.models;

import no.kristiania.pgr200.common.Models.Topic;
import no.kristiania.pgr200.orm.BaseRecord;

public class TopicModel extends BaseRecord<TopicModel, Topic> {

    public TopicModel(String topic) {
        super(new Topic(topic));
    }

    @Override
    public String getTable() {
        return "topics";
    }
}
