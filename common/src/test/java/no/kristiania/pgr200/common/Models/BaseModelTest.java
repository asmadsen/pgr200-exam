package no.kristiania.pgr200.common.Models;

import com.github.javafaker.Faker;
import no.kristiania.pgr200.orm.ColumnValue;
import org.junit.Test;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.HashMap;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

public class BaseModelTest {
    Faker faker = new Faker();

    @Test
    public void shouldPopulateModel() {
        User user = new User();
        HashMap<String, ColumnValue> attributes = new HashMap<>();
        String name = faker.funnyName().name();
        attributes.put("name", new ColumnValue<>(name));
        int age = faker.number().numberBetween(18, 58);
        attributes.put("age", new ColumnValue<>(age));
        user.populateAttributes(attributes);
        assertThat(user).hasFieldOrPropertyWithValue("name", name);
        assertThat(user).hasFieldOrPropertyWithValue("age", age);
    }

    @Test
    public void shouldFetchAttributesAsMap() {
        User user = new User();
        HashMap<String, ColumnValue> attributes = new HashMap<>();
        String name = faker.funnyName().name();
        attributes.put("name", new ColumnValue<>(name));
        user.name = name;
        int age = faker.number().numberBetween(18, 58);
        attributes.put("age", new ColumnValue<>(age));
        user.age = age;
        assertThat(user.getAttributes()).containsAllEntriesOf(attributes);
    }

    @Test
    public void shouldValidateAccordingToModelRules() {
        User user = new User();
        assertThat(user.validate()).hasSize(2);

        user.name = faker.rickAndMorty().character();
        user.age = faker.number().numberBetween(18, 58);
        assertThat(user.validate()).isEmpty();
    }
}

class User extends BaseModel<User> {
    protected UUID id;
    @NotNull
    public String name;
    @NotNull @Min(18)
    public int age;

    @Override
    public void setAttribute(String column, Object value) {

    }

    @Override
    public ColumnValue getAttribute(String column) {
        return null;
    }
}
