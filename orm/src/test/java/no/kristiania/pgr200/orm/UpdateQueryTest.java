package no.kristiania.pgr200.orm;

import no.kristiania.pgr200.orm.enums.SqlOperator;
import no.kristiania.pgr200.orm.test_data.UserModel;
import org.junit.Test;

import java.sql.PreparedStatement;
import java.sql.SQLException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

public class UpdateQueryTest {


    @Test
    public void shouldComposeUpdateStatement() {
        UpdateQuery<UserModel> query = new UpdateQuery<>(new UserModel()).set("name", "some name");

        assertThat(query.getSqlStatement()).isEqualTo("UPDATE `users` SET `name` = ?");
    }

    @Test
    public void shouldComposeWhereStatement() {
        UpdateQuery<UserModel> query = new UpdateQuery<UserModel>(new UserModel()).set("name", "Ola Nordmann")
                                                                                  .where("id", SqlOperator.Equals, 1);

        assertThat(query.getSqlStatement()).isEqualTo("UPDATE `users` SET `name` = ? WHERE `id` = ?");

        query = new UpdateQuery<UserModel>(new UserModel()).set("name", "Ola Nordmann")
                                                           .whereEquals("id", 1);

        assertThat(query.getSqlStatement()).isEqualTo("UPDATE `users` SET `name` = ? WHERE `id` = ?");

        query = new UpdateQuery<UserModel>(new UserModel()).set("name", "Ola Nordmann")
                                                           .whereNot("id", 1);

        assertThat(query.getSqlStatement()).isEqualTo("UPDATE `users` SET `name` = ? WHERE `id` NOT ?");

        query = new UpdateQuery<UserModel>(new UserModel()).set("name", "Ola Nordmann")
                                                           .whereIsNull("name");

        assertThat(query.getSqlStatement()).isEqualTo("UPDATE `users` SET `name` = ? WHERE `name` IS NULL");
    }

    @Test
    public void shouldInsertDifferentDataTypes() throws SQLException {
        UpdateQuery<UserModel> query = new UpdateQuery<UserModel>(new UserModel())
                .where("name", SqlOperator.Equals, "John Doe")
                .whereIsNull("email")
                .where("age", SqlOperator.GreaterThan, 18);

        PreparedStatement statement = mock(PreparedStatement.class);
        query.populateStatement(statement);

        verify(statement).setObject(eq(1), eq("John Doe"));
        verify(statement).setObject(eq(2), eq(18));
        verify(statement, times(2)).setObject(anyInt(), any());
    }

    @Test
    public void shouldPopulateStatement() throws SQLException {
        UpdateQuery<UserModel> query = new UpdateQuery<>(new UserModel())
                .set("name", "John Doe")
                .whereEquals("id", 15);

        PreparedStatement statement = mock(PreparedStatement.class);
        query.populateStatement(statement);

        verify(statement).setObject(eq(1), eq("John Doe"));
        verify(statement).setObject(eq(2), eq(15));
        verify(statement, times(2)).setObject(anyInt(), any());
    }
}
