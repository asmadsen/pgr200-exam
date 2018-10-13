package no.kristiania.pgr200.orm;

import no.kristiania.pgr200.orm.TestData.UserModel;
import org.junit.Ignore;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class JoinStatementTest {
    @Test
    public void shouldParseRegularJoin() {
        JoinStatement<UserModel> statement = new JoinStatement<>(
                new UserModel(),
                "id",
                "user_id"
        );

        assertThat(statement.getSqlStatement()).isEqualTo("LEFT JOIN `users` ON `users`.`id` = `?`.`user_id`");
    }

    @Test
    public void shouldParseDifferentJoinTypes() {
        JoinStatement<UserModel> statement = new JoinStatement<>(
                new UserModel(),
                "id",
                "user_id",
                JoinType.LeftJoin
        );
        assertThat(statement.getSqlStatement()).startsWith("LEFT JOIN ");

        statement = new JoinStatement<>(
                new UserModel(),
                "id",
                "user_id",
                JoinType.InnerJoin
        );
        assertThat(statement.getSqlStatement()).startsWith("INNER JOIN ");

        statement = new JoinStatement<>(
                new UserModel(),
                "id",
                "user_id",
                JoinType.FullOuterJoin
        );
        assertThat(statement.getSqlStatement()).startsWith("FULL OUTER JOIN ");

        statement = new JoinStatement<>(
                new UserModel(),
                "id",
                "user_id",
                JoinType.RightJoin
        );
        assertThat(statement.getSqlStatement()).startsWith("RIGHT JOIN ");
    }
}
