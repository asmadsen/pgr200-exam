package no.kristiania.pgr200.orm;

import org.junit.Test;

import java.sql.PreparedStatement;
import java.sql.SQLException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

public class UpdateQueryTest {


    @Test
    public void shouldComposeUpdateStatement(){
        UpdateQuery query = new UpdateQuery("users").set("name", "some name").whereEquals("id", 15);

        assertThat(query.getSqlStatement()).isEqualTo("UPDATE `users` SET `name` = ? WHERE `id` = ?;");
    }

    @Test
    public void shouldPopulateStatement() throws SQLException {
        UpdateQuery query = new UpdateQuery("users")
                .set("name", "John Doe")
                .whereEquals("id", 15);

        PreparedStatement statement = mock(PreparedStatement.class);
        query.populateStatement(statement);

        verify(statement).setObject(eq(1), eq("John Doe"));
        verify(statement).setObject(eq(2), eq(15));
        verify(statement, times(2)).setObject(anyInt(), any());
    }
}
