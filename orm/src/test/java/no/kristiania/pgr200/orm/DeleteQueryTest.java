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

public class DeleteQueryTest {


    @Test
    public void shouldComposeDeleteStatement() {
        DeleteQuery query = new DeleteQuery("users");

        assertThat(query.getSqlStatement()).isEqualTo("DELETE FROM `users`");
    }

    @Test
    public void shouldComposeWhereStatement() {
        DeleteQuery query = new DeleteQuery<UserModel>("users").where("id", SqlOperator.Equals, 1);

        assertThat(query.getSqlStatement()).isEqualTo("DELETE FROM `users` WHERE `id` = ?");

        query = new DeleteQuery<UserModel>("users").whereEquals("id", 1);

        assertThat(query.getSqlStatement()).isEqualTo("DELETE FROM `users` WHERE `id` = ?");

        query = new DeleteQuery<UserModel>("users").whereNot("id", 1);

        assertThat(query.getSqlStatement()).isEqualTo("DELETE FROM `users` WHERE `id` NOT ?");

        query = new DeleteQuery<UserModel>("users").whereIsNull("name");

        assertThat(query.getSqlStatement()).isEqualTo("DELETE FROM `users` WHERE `name` IS NULL");
    }

    @Test
    public void shouldInsertDifferentDataTypes() throws SQLException {
        DeleteQuery query = new DeleteQuery<UserModel>("users")
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
        DeleteQuery query = new DeleteQuery<>("users")
                .whereEquals("id", 15);

        PreparedStatement statement = mock(PreparedStatement.class);
        query.populateStatement(statement);

        verify(statement).setObject(eq(1), eq(15));
        verify(statement, times(1)).setObject(anyInt(), anyInt());
    }

    @Test
    public void shouldBuildDeleteWithoutWheres() {
        DeleteQuery query = new DeleteQuery("users");
        assertThat(query.getSqlStatement()).isEqualTo("DELETE FROM `users`");
    }
}
