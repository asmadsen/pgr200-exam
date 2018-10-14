package no.kristiania.pgr200.orm;

import no.kristiania.pgr200.orm.Enums.OrderDirection;
import no.kristiania.pgr200.orm.Enums.SqlOperator;
import no.kristiania.pgr200.orm.TestData.UserModel;
import org.junit.Ignore;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class QueryTest {
    @Test
    public void shouldComposeRegularSelectFromTable() {
        Query query = new Query("users", "id", "name");

        assertThat(query.getSqlStatement()).isEqualTo("SELECT `id`, `name` FROM `users`");
    }

    @Test
    public void shouldComposeGroupedQuery() {
        Query query = new Query("users", "age")
                .count("id")
                .groupBy("age");

        assertThat(query.getSqlStatement()).isEqualTo("SELECT `age`, COUNT(`id`) FROM `users` GROUP BY `age`");

        query = new Query("users", "age")
                .count("id")
                .groupBy("age", "name");

        assertThat(query.getSqlStatement()).isEqualTo("SELECT `age`, COUNT(`id`) FROM `users` GROUP BY `age`, `name`");
    }

    @Test
    public void shouldComposeOrderedQuery() {
        Query query = new Query("users", "id", "name", "age")
                .orderBy("age");

        assertThat(query.getSqlStatement()).isEqualTo("SELECT `id`, `name`, `age` FROM `users` ORDER BY `age` DESC");

        query = new Query("users", "id", "name", "age")
                .orderBy("age")
                .orderBy("name", OrderDirection.ASC);

        assertThat(query.getSqlStatement()).isEqualTo("SELECT `id`, `name`, `age` FROM `users` ORDER BY `age` DESC, `name` ASC");
    }

    @Test
    public void shouldComposeAggregators() {
        Query query = new Query("users", "id")
                .count("id");

        assertThat(query.getSqlStatement()).isEqualTo("SELECT `id`, COUNT(`id`) FROM `users`");

        query = new Query("users", "id")
            .average("id");

        assertThat(query.getSqlStatement()).isEqualTo("SELECT `id`, AVG(`id`) FROM `users`");

        query = new Query("users", "id")
                .sum("fortune");

        assertThat(query.getSqlStatement()).isEqualTo("SELECT `id`, SUM(`fortune`) FROM `users`");

        query = new Query("users", "id")
                .max("fortune");

        assertThat(query.getSqlStatement()).isEqualTo("SELECT `id`, MAX(`fortune`) FROM `users`");

        query = new Query("users", "id")
                .min("fortune");

        assertThat(query.getSqlStatement()).isEqualTo("SELECT `id`, MIN(`fortune`) FROM `users`");
    }

    @Test
    public void shouldComposeJoinQuery() {
        Query query = new Query("roles", "id", "name")
                .join(new UserModel(), "id", "user_id")
                .select("users.id", "users.name");

        assertThat(query.getSqlStatement()).isEqualTo("SELECT `id`, `name`, `users`.`id`, `users`.`name` FROM `roles` LEFT JOIN `users` ON `users`.`id` = `roles`.`user_id`");
    }

    @Ignore
    @Test
    public void shouldComposeWhereStatement() {
        Query query = new Query("users", "id", "name")
                .where("id", SqlOperator.Equals, 1);

        assertThat(query.getSqlStatement()).isEqualTo("SELECT `id`, `name` FROM `users` WHERE `id` = 1");
    }

    @Ignore
    @Test
    public void shouldComposeMultiWhereStatement() {
        Query query = new Query("users", "id", "name", "age")
                .where("name", SqlOperator.Equals, "John Doe")
                .where("age", SqlOperator.GreaterThan, 18);

        assertThat(query.getSqlStatement()).isEqualTo("SELECT `id`, `name`, `age` FROM `users` WHERE `name` = \"John Doe\" AND `age` > 18");

        query = new Query("users", "id", "name", "age")
                .where("name", SqlOperator.Equals, "John Doe")
                .where("name", SqlOperator.Equals, "Jane Doe", true);

        assertThat(query.getSqlStatement()).isEqualTo("SELECT `id`, `name`, `age` FROM `users` WHERE `name` = \"John Doe\" OR `name` = \"Jane Doe\"");
    }
}
