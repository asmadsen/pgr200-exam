package no.kristiania.pgr200.orm.TestData;

import no.kristiania.pgr200.orm.BaseRecord;

import java.util.UUID;

public class ProfilePictureModel extends BaseRecord<ProfilePictureModel, ProfilePicture> {

    public ProfilePictureModel() {
        super(new ProfilePicture());
    }

    public ProfilePictureModel(UUID id, UUID userId, String pictureUrl) {
        super(new ProfilePicture(id, userId, pictureUrl));
    }

    public ProfilePictureModel(UUID userId, String pictureUrl) {
        super(new ProfilePicture(null, userId, pictureUrl));
    }

    @Override
    public String getTable() {
        return "profile_picture";
    }

    /*
    @Relation
    public HasOne<UserModel> user() {
        return this.hasOne(UserModel.class, "id", "userId");
    }
    */
}
