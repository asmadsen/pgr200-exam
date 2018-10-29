package no.kristiania.pgr200.orm.TestData;

import no.kristiania.pgr200.orm.BaseRecord;

import java.util.UUID;

public class PostModel extends BaseRecord<PostModel, Post> {

    public PostModel() {
        super(new Post());
    }

    public PostModel(UUID id, String title, String slug, String content, UUID userId) {
        super(new Post(id, title, slug, content, userId));
    }

    public PostModel(Post state) {
        super(state);
    }

    public static PostModel Create(UUID id, String title, String slug, String content, UUID userId) {
        PostModel postModel = new PostModel(id, title, slug, content, userId);
        postModel.save();
        return postModel;
    }

    @Override
    public String getTable() {
        return "posts";
    }
}
