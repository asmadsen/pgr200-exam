package no.kristiania.pgr200.orm.TestData;

import no.kristiania.pgr200.orm.Annotations.Relation;
import no.kristiania.pgr200.orm.BaseRecord;
import no.kristiania.pgr200.orm.Relations.HasOne;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class ProfilePictureModel extends BaseRecord<ProfilePicture> {

    public ProfilePictureModel() {
        this.setState(new ProfilePicture());
    }

    public ProfilePictureModel(UUID id, UUID userId, String pictureUrl) {
        this.setState(new ProfilePicture(id, userId, pictureUrl));
    }

    public ProfilePictureModel(UUID userId, String pictureUrl) {
        this.setState(new ProfilePicture(null, userId, pictureUrl));
    }

    @Override
    public String getTable() {
        return "profile_picture";
    }

    @Relation
    public HasOne<UserModel> user() {
        return new HasOne<>(UserModel.class, "id", "userId");
    }
}
