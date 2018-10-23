package no.kristiania.pgr200.orm.TestData;

import no.kristiania.pgr200.orm.ColumnValue;
import no.kristiania.pgr200.orm.IBaseModel;

import javax.validation.ConstraintViolation;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

public class ProfilePicture extends BaseModel<ProfilePicture> {
    protected UUID id;
    public UUID userId;
    public String pictureUrl;

    public ProfilePicture() {
    }

    public ProfilePicture(UUID id, UUID userId, String pictureUrl) {
        this.id = id;
        this.userId = userId;
        this.pictureUrl = pictureUrl;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getUserId() {
        return userId;
    }

    public void setUserId(UUID userId) {
        this.userId = userId;
    }

    public String getPictureUrl() {
        return pictureUrl;
    }

    public void setPictureUrl(String pictureUrl) {
        this.pictureUrl = pictureUrl;
    }
}
