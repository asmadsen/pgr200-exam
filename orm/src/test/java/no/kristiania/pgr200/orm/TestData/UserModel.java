package no.kristiania.pgr200.orm.TestData;

import no.kristiania.pgr200.orm.BaseRecord;

import java.sql.ResultSet;
import java.sql.SQLException;
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

    @Override
    public User newInstance(ResultSet resultSet) throws SQLException {
        return new User(UUID.fromString(resultSet.getString("id")),
                resultSet.getString("name"),
                resultSet.getString("email"));
    }
}
