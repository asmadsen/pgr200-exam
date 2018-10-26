package no.kristiania.pgr200.orm.TestData;

import no.kristiania.pgr200.orm.Annotations.Relation;
import no.kristiania.pgr200.orm.BaseRecord;
import no.kristiania.pgr200.orm.Relations.AbstractRelation;

import java.util.UUID;

public class UserModel extends BaseRecord<UserModel, User>{

    public AbstractRelation<PhoneModel, Phone, UserModel> phoneRelation;

    public UserModel() {
        super(new User());
    }

    public UserModel(UUID id, String name, String email){
        super(new User(id, name, email));
    }

    public UserModel(String fullName, String emailAddress) {
        this(null, fullName, emailAddress);
    }

    @Override
    public String getTable() {
        return "users";
    }

    @Relation
    public AbstractRelation<PhoneModel, Phone, UserModel> phone() {
        return phoneRelation;
    }

    /*
    @Relation
    public HasOne<PhoneModel> phone() {
        return this.hasOne(PhoneModel.class, "userId", "id");
    }

    @Relation
    public HasMany<ProfilePictureModel> profilePictures() {
        return this.hasMany(ProfilePictureModel.class, "userId", "id");
    }

    @Relation
    public HasManyThrough<UserModel> friends() {
        return new HasManyThrough<>(UserModel.class, "friends_table", "userId", "id", "friendId", "id");
    }
    */
}
