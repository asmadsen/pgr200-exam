package no.kristiania.pgr200.common.Models;

import java.util.Objects;
import java.util.UUID;

public class Talk extends BaseModel<Talk> {

    protected UUID id;
    public String title;
    public String description;
//    public UUID topic_id;

    public Talk() {
    }

    public Talk(String title, String description){
        setTitle(title);
        setDescription(description);

    }

    public Talk(UUID id, String title, String description){
        setId(id);
        setTitle(title);
        setDescription(description);
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

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Talk) {
            return this.getId().equals(((Talk) obj).getId()) &&
                    this.getTitle().equals(((Talk) obj).getTitle()) &&
                    this.getDescription().equals(((Talk) obj).getDescription());
        }
        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, description);
    }
}
