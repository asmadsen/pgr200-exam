package no.kristiania.pgr200.orm;

import no.kristiania.pgr200.orm.enums.OrderDirection;
import no.kristiania.pgr200.orm.enums.SqlOperator;
import no.kristiania.pgr200.orm.test_data.User;
import no.kristiania.pgr200.orm.test_data.UserModel;
import org.junit.Test;

import java.sql.PreparedStatement;
import java.sql.SQLException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

public class SelectQueryTest {
    @Test
    public void shouldComposeRegularSelectFromTable() {
        SelectQuery<UserModel, User> query = new SelectQuery<>((UserModel) (new UserModel()), "id", "name");

        assertThat(query.getSqlStatement()).isEqualTo("SELECT `id`, `name` FROM `users`");
    }

    @Test
    public void shouldComposeGroupedQuery() {
        SelectQuery query = new SelectQuery<>(new UserModel(), "age")
                .count("id")
                .groupBy("age");

        assertThat(query.getSqlStatement()).isEqualTo("SELECT `age`, COUNT(`id`) FROM `users` GROUP BY `age`");

        query = new SelectQuery<>(new UserModel(), "age")
                .count("id")
                .groupBy("age", "name");

        assertThat(query.getSqlStatement()).isEqualTo("SELECT `age`, COUNT(`id`) FROM `users` GROUP BY `age`, `name`");
    }

    @Test
    public void shouldComposeOrderedQuery() {
        SelectQuery query = new SelectQuery<>(new UserModel(), "id", "name", "age")
                .orderBy("age");

        assertThat(query.getSqlStatement()).isEqualTo("SELECT `id`, `name`, `age` FROM `users` ORDER BY `age` DESC");

        query = new SelectQuery<>(new UserModel(), "id", "name", "age")
                .orderBy("age")
                .orderBy("name", OrderDirection.ASC);

        assertThat(query.getSqlStatement()).isEqualTo(
                "SELECT `id`, `name`, `age` FROM `users` ORDER BY `age` DESC, `name` ASC");
    }

    @Test
    public void shouldComposeAggregators() {
        SelectQuery query = new SelectQuery<>(new UserModel(), "id")
                .count("id");

        assertThat(query.getSqlStatement()).isEqualTo("SELECT `id`, COUNT(`id`) FROM `users`");

        query = new SelectQuery<>(new UserModel(), "id")
                .average("id");

        assertThat(query.getSqlStatement()).isEqualTo("SELECT `id`, AVG(`id`) FROM `users`");

        query = new SelectQuery<>(new UserModel(), "id")
                .sum("fortune");

        assertThat(query.getSqlStatement()).isEqualTo("SELECT `id`, SUM(`fortune`) FROM `users`");

        query = new SelectQuery<>(new UserModel(), "id")
                .max("fortune");

        assertThat(query.getSqlStatement()).isEqualTo("SELECT `id`, MAX(`fortune`) FROM `users`");

        query = new SelectQuery<>(new UserModel(), "id")
                .min("fortune");

        assertThat(query.getSqlStatement()).isEqualTo("SELECT `id`, MIN(`fortune`) FROM `users`");
    }

    @Test
    public void shouldComposeWhereStatement() {
        SelectQuery query = new SelectQuery<>(new UserModel(), "id", "name")
                .where("id", SqlOperator.Equals, 1);

        assertThat(query.getSqlStatement()).isEqualTo("SELECT `id`, `name` FROM `users` WHERE `id` = ?");

        query = new SelectQuery<>(new UserModel(), "id", "name")
                .whereEquals("id", 1);

        assertThat(query.getSqlStatement()).isEqualTo("SELECT `id`, `name` FROM `users` WHERE `id` = ?");

        query = new SelectQuery<>(new UserModel(), "id", "name")
                .whereNot("id", 1);

        assertThat(query.getSqlStatement()).isEqualTo("SELECT `id`, `name` FROM `users` WHERE `id` NOT ?");

        query = new SelectQuery<>(new UserModel(), "id", "name")
                .whereIsNull("name");

        assertThat(query.getSqlStatement()).isEqualTo("SELECT `id`, `name` FROM `users` WHERE `name` IS NULL");
    }

    @Test
    public void shouldComposeMultiWhereStatement() {
        SelectQuery query = new SelectQuery<>(new UserModel(), "id", "name", "age")
                .where("name", SqlOperator.Equals, "John Doe")
                .where("age", SqlOperator.GreaterThan, 18);

        assertThat(query.getSqlStatement()).isEqualTo(
                "SELECT `id`, `name`, `age` FROM `users` WHERE `name` = ? AND `age` > ?");

        query = new SelectQuery<>(new UserModel(), "id", "name", "age")
                .where("name", SqlOperator.Equals, "John Doe")
                .where("name", SqlOperator.Equals, "Jane Doe", false);

        assertThat(query.getSqlStatement()).isEqualTo(
                "SELECT `id`, `name`, `age` FROM `users` WHERE `name` = ? OR `name` = ?");
    }

    @Test
    public void shouldInsertDifferentDataTypes() throws SQLException {
        SelectQuery query = new SelectQuery<>(new UserModel(), "id", "name", "age")
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
    public void shouldComposeLimitStatement() {
        SelectQuery query = new SelectQuery<>(new UserModel(), "id", "name", "age").limit(5);
        assertThat(query.getSqlStatement()).isEqualTo("SELECT `id`, `name`, `age` FROM `users` LIMIT 5");
    }

    @Test
    public void shouldTrowInvalidArgumentWhenNegativeLimit() {
        assertThatThrownBy(() -> {
            new SelectQuery<>(new UserModel(), "id", "name", "age").limit(-1);
        }).isInstanceOf(IllegalArgumentException.class);
    }
}
