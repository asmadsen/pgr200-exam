package no.kristiania.pgr200.orm.test_data;

import java.util.UUID;

public class Post extends BaseModel<Post> {
    protected UUID id;
    public String title;
    public String slug;
    public String content;
    public UUID user_id;

    public Post() {
    }

    public Post(UUID id, String title, String slug, String content, UUID user_id) {
        this.id = id;
        this.title = title;
        this.slug = slug;
        this.content = content;
        this.user_id = user_id;
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

    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public UUID getUserId() {
        return user_id;
    }

    public void setUserId(UUID user_id) {
        this.user_id = user_id;
    }

    @Override
    public boolean equals(Object other) {
        if (other instanceof Post) {
            return this.getId().equals(((Post) other).getId()) &&
                    this.getTitle().equals(((Post) other).getTitle()) &&
                    this.getSlug().equals(((Post) other).getSlug()) &&
                    this.getContent().equals(((Post) other).getContent()) &&
                    this.getUserId().equals(((Post) other).getUserId());
        }
        return false;
    }

    @Override
    public int hashCode() {
        return 0;
    }
}
