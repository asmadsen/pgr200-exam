package no.kristiania.pgr200.orm;

import org.junit.Test;

import java.sql.PreparedStatement;
import java.sql.SQLException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

public class DeleteQueryTest {


    @Test
    public void shouldComposeDeleteStatement(){
        DeleteQuery query = new DeleteQuery("users").whereEquals("id", 15);

        assertThat(query.getSqlStatement()).isEqualTo("DELETE FROM `users` WHERE `id` = ?");
    }

    @Test
    public void shouldPopulateStatement() throws SQLException {
        DeleteQuery query = new DeleteQuery("users")
                .whereEquals("id", 15);

        PreparedStatement statement = mock(PreparedStatement.class);
        query.populateStatement(statement);

        verify(statement).setObject(eq(1), eq(15));
        verify(statement, times(1)).setObject(anyInt(), anyInt());
    }

    @Test
    public void shouldBuildDeleteWithoutWheres(){
         DeleteQuery query = new DeleteQuery("users");
        assertThat(query.getSqlStatement()).isEqualTo("DELETE FROM `users`");
    }
}
