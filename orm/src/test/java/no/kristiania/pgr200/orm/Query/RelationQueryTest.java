package no.kristiania.pgr200.orm.Query;

import no.kristiania.pgr200.orm.Relations.AbstractRelation;
import no.kristiania.pgr200.orm.SelectQuery;
import no.kristiania.pgr200.orm.TestData.Phone;
import no.kristiania.pgr200.orm.TestData.PhoneModel;
import no.kristiania.pgr200.orm.TestData.User;
import no.kristiania.pgr200.orm.TestData.UserModel;
import org.junit.Test;
import org.mockito.ArgumentMatchers;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class RelationQueryTest {
    @Test
    public void shouldCallRelationMethods() throws SQLException {
        UserModel userModel = new UserModel();
        AbstractRelation<PhoneModel, Phone, UserModel> phoneMock = mock(AbstractRelation.class);
        userModel.phoneRelation = phoneMock;

        SelectQuery<UserModel, User> query = userModel.newQuery().with("phone");

        List<UserModel> users = new ArrayList<>();

        UserModel user = (UserModel) userModel.newModelInstance();

        user.fill(new User(null, "Ola Nordmann", "ola@nordmann.no").getAttributes());
        users.add(user);

        query.eagerLoadRelations(users);

        verify(phoneMock).addEagerConstraints(ArgumentMatchers.eq(users));
        verify(phoneMock).initRelation(eq(users), eq("phone"));
    }
}
