package no.kristiania.pgr200.orm.TestData;

import no.kristiania.pgr200.orm.BaseRecord;
import no.kristiania.pgr200.orm.Query;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;
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
    public List<User> all() throws SQLException {
        ResultSet results = queryStatement(new Query<User>(this.getTable(), state.getAttributes().keySet().toArray(new String[0])));
        List<User> list = new LinkedList<>();
        while (results.next()) {
            list.add(new User(UUID.fromString(results.getString("id")), results.getString("name"), results.getString("email")));
        }
        return list;
    }
}
