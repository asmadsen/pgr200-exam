package no.kristiania.pgr200.orm;

import no.kristiania.pgr200.orm.Enums.JoinType;
import no.kristiania.pgr200.orm.TestData.UserModel;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class JoinStatementTest {
    @Test
    public void shouldParseRegularJoin() {
        JoinStatement<UserModel> statement = new JoinStatement<>(
                new UserModel(),
                "id",
                "user_id"
        );

        assertThat(statement.getSqlStatement("roles")).isEqualTo("LEFT JOIN `users` ON `users`.`id` = `roles`.`user_id`");
    }

    @Test
    public void shouldParseSubQueryJoin() {
        Query<GenericRecord> mockQuery = mock(Query.class);
        String subQuery = "SELECT * FROM `USERS` WHERE `id` = 1";
        when(mockQuery.getSqlStatement()).thenReturn(subQuery);
        JoinStatement<GenericRecord> statement = new JoinStatement<>(
                mockQuery,
                "users",
                "id",
                "user_id"
        );

        assertThat(statement.getSqlStatement("roles")).isEqualTo("LEFT JOIN (" + subQuery + ") ON `users`.`id` = `roles`.`user_id`");
    }

    @Test
    public void shouldParseDifferentJoinTypes() {
        JoinStatement<UserModel> statement = new JoinStatement<>(
                new UserModel(),
                "id",
                "user_id",
                JoinType.LeftJoin
        );
        assertThat(statement.getSqlStatement("")).startsWith("LEFT JOIN ");

        statement = new JoinStatement<>(
                new UserModel(),
                "id",
                "user_id",
                JoinType.InnerJoin
        );
        assertThat(statement.getSqlStatement("")).startsWith("INNER JOIN ");

        statement = new JoinStatement<>(
                new UserModel(),
                "id",
                "user_id",
                JoinType.FullOuterJoin
        );
        assertThat(statement.getSqlStatement("")).startsWith("FULL OUTER JOIN ");

        statement = new JoinStatement<>(
                new UserModel(),
                "id",
                "user_id",
                JoinType.RightJoin
        );
        assertThat(statement.getSqlStatement("")).startsWith("RIGHT JOIN ");
    }
}
