package no.kristiania.pgr200.orm.TestData;

import no.kristiania.pgr200.orm.BaseRecord;

import java.util.UUID;

public class UserModel extends BaseRecord<User>{

    public UserModel() {
        super();
        setState(new User());
    }

    public UserModel(UUID id, String name, String email){
        setState(new User(id, name, email));
    }

    public UserModel(String fullName, String emailAddress) {
        this(null, fullName, emailAddress);
    }

    @Override
    public String getTable() {
        return "users";
    }
}
