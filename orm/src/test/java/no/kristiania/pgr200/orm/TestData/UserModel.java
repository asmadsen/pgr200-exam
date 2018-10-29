package no.kristiania.pgr200.orm.TestData;

import no.kristiania.pgr200.orm.Annotations.Relation;
import no.kristiania.pgr200.orm.BaseRecord;
import no.kristiania.pgr200.orm.Relations.AbstractRelation;
import no.kristiania.pgr200.orm.Relations.HasMany;
import no.kristiania.pgr200.orm.Relations.HasOne;

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

    public static UserModel Create(UUID id, String name, String email) {
        UserModel userModel = new UserModel(id, name, email);
        userModel.save();
        return userModel;
    }

    @Override
    public String getTable() {
        return "users";
    }

    @Relation
    public HasOne<PhoneModel, Phone, UserModel> phone() {
        return this.hasOne(new PhoneModel(), "user_id", "id");
    }

    @Relation
    public HasMany<PostModel, Post, UserModel> posts() {
        return this.hasMany(new PostModel(), "user_id", "id");
    }
}
