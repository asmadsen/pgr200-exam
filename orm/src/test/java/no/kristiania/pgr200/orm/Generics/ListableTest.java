package no.kristiania.pgr200.orm.Generics;

import com.github.javafaker.Faker;
import no.kristiania.pgr200.orm.TestData.User;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class ListableTest {
    Faker faker = new Faker();

    @Test
    public void shouldBeTrueIfTypeIsList() {
        User user = new User(null, faker.funnyName().name(), faker.internet().emailAddress());
        Listable<User> listable = new Listable<>(user);
        assertThat(listable.isList()).isFalse();
        List<User> userList = new ArrayList<>();
        userList.add(user);
        listable = new Listable<>(userList);
        assertThat(listable.isList()).isTrue();
    }

    @Test
    public void shouldGuardGettersBasedOnType() {
        User user = new User(null, faker.funnyName().name(), faker.internet().emailAddress());
        Listable<User> listable = new Listable<>(user);

        assertThat(listable.getValue()).isEqualTo(user);
        assertThat(listable.getListValue()).containsExactly(user);

        List<User> userList = new LinkedList<>();
        userList.add(user);
        listable = new Listable<>(userList);
        assertThat(listable.getValue()).isNull();
        assertThat(listable.getListValue()).isExactlyInstanceOf(LinkedList.class)
                                           .isEqualTo(userList);

        userList = null;
        listable = new Listable<>(userList);
        assertThat(listable.getListValue()).isInstanceOf(Collection.class).isEmpty();
    }
}
