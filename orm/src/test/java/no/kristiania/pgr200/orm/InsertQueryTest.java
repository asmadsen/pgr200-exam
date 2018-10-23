package no.kristiania.pgr200.orm;

import no.kristiania.pgr200.orm.TestData.UserModel;
import org.junit.Test;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

public class InsertQueryTest {

    @Test
    public void shouldComposeInsertStatement(){
        UserModel model = new UserModel(UUID.randomUUID(), "John Doe", "john@example.com");
        InsertQuery query = new InsertQuery("users").insert(model);

        assertThat(query.getSqlStatement()).isEqualTo("INSERT INTO `users` (`name`, `id`, `email`) VALUES (?, ?, ?);");
    }

    @Test
    public void shouldPopulateStatement() throws SQLException {
        UserModel model = new UserModel(UUID.randomUUID(), "John Doe", "john@example.com");
        InsertQuery query = new InsertQuery("users").insert(model);

        PreparedStatement statement = mock(PreparedStatement.class);
        query.populateStatement(statement);

        verify(statement).setObject(eq(1), eq(model.getState().getName()));
        verify(statement).setObject(eq(2), eq(model.getState().getId()));
        verify(statement).setObject(eq(3), eq(model.getState().getEmail()));
        verify(statement, times(3)).setObject(anyInt(), any());
    }
}
