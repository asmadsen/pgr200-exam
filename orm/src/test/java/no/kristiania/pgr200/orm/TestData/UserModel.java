package no.kristiania.pgr200.orm.TestData;

import no.kristiania.pgr200.orm.Annotations.Relation;
import no.kristiania.pgr200.orm.BaseRecord;
import no.kristiania.pgr200.orm.ColumnValue;
import no.kristiania.pgr200.orm.IBaseModel;
import no.kristiania.pgr200.orm.Relations.HasMany;
import no.kristiania.pgr200.orm.Relations.HasManyThrough;
import no.kristiania.pgr200.orm.Relations.HasOne;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class UserModel extends BaseRecord<User>{

    public UserModel() {
        super();
        model = new User();
    }

    public UserModel(UUID id, String name, String email){
        model = new User();
        model.setId(id);
        model.setName(name);
        model.setEmail(email);
    }

    public UserModel(String fullName, String emailAddress) {
        this(null, fullName, emailAddress);
    }

    @Override
    public String getTable() {
        return "users";
    }

    @Relation
    public HasOne<PhoneModel> phone() {
        return this.hasOne(PhoneModel.class, "userId", "id");
    }

    @Relation
    public HasMany<ProfilePictureModel> profilePictures() {
        return new HasMany<>(ProfilePictureModel.class, "userId", "id");
    }

    @Relation
    public HasManyThrough<UserModel> friends() {
        return new HasManyThrough<>(UserModel.class, "friends_table", "userId", "id", "friendId", "id");
    }
}
