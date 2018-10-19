package no.kristiania.pgr200.orm.TestData;

import no.kristiania.pgr200.orm.BaseRecord;
import no.kristiania.pgr200.orm.ColumnValue;
import no.kristiania.pgr200.orm.IBaseModel;

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

    public User getUser() {
        return this.model;
    }
}
