package no.kristiania.pgr200.orm.query;

import com.github.javafaker.Faker;
import no.kristiania.pgr200.orm.TestUtils;
import no.kristiania.pgr200.orm.test_data.PhoneModel;
import no.kristiania.pgr200.orm.test_data.PostModel;
import no.kristiania.pgr200.orm.test_data.UserModel;
import org.junit.Before;
import org.junit.Test;

import java.sql.SQLException;
import java.util.Collection;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

public class RelationQueryTest {
    Faker faker = new Faker();

    @Before
    public void setupDb() throws SQLException {
        TestUtils.setupDatabase();
    }

    @Test
    public void shouldGetHasOneRelationship() {
        UserModel userModel = UserModel.Create(null, faker.funnyName().name(), faker.internet().emailAddress());
        PhoneModel phoneModel = PhoneModel.Create(null,
                                                  userModel.getPrimaryKeyValue(),
                                                  faker.phoneNumber().cellPhone());

        UserModel result = userModel.newQuery().with("phone").whereEquals("id", userModel.getPrimaryKeyValue()).first();
        assertThat(result.getState()).isEqualTo(userModel.getState());
        assertThat(result.getRelation("phone", PhoneModel.class)
                         .getValue()
                         .getState()).isEqualTo(phoneModel.getState());
    }

    @Test
    public void shouldGetBelongsToRelationship() {
        UserModel userModel = UserModel.Create(null, faker.funnyName().name(), faker.internet().emailAddress());
        PhoneModel phoneModel = PhoneModel.Create(null,
                                                  userModel.getPrimaryKeyValue(),
                                                  faker.phoneNumber().cellPhone());

        assertThat(phoneModel.getRelation("user", UserModel.class).getValue()).isEqualTo(userModel);

        PhoneModel result = phoneModel.newQuery()
                                      .with("user")
                                      .whereEquals("id", phoneModel.getPrimaryKeyValue())
                                      .first();
        assertThat(result.getState()).isEqualTo(phoneModel.getState());
        assertThat(result.getRelation("user", UserModel.class).getValue().getState()).isEqualTo(userModel.getState());
    }

    @Test
    public void shouldGetHasMany() {
        UserModel userModel = UserModel.Create(null, faker.funnyName().name(), faker.internet().emailAddress());
        PostModel post1 = PostModel.Create(null,
                                           faker.name().title(),
                                           faker.internet().slug(),
                                           faker.lorem().paragraph(),
                                           userModel.getPrimaryKeyValue());
        PostModel post2 = PostModel.Create(null,
                                           faker.name().title(),
                                           faker.internet().slug(),
                                           faker.lorem().paragraph(),
                                           userModel.getPrimaryKeyValue());

        assertThat(userModel.getRelation("posts", PostModel.class).getListValue())
                .isInstanceOf(Collection.class)
                .containsOnlyOnce(post1, post2);

        UserModel result = userModel.newQuery().with("posts").whereEquals("id", userModel.getPrimaryKeyValue()).first();
        assertThat(result.getState()).isEqualTo(userModel.getState());
        Collection<PostModel> posts = result.getRelation("posts", PostModel.class)
                                            .getListValue();

        assertThat(posts)
                .isInstanceOf(Collection.class)
                .containsOnlyOnce(post1, post2);
    }

    @Test
    public void shouldThrowExceptionWhenQueryingForUndefinedRelation() {
        UserModel userModel = UserModel.Create(null, faker.funnyName().name(), faker.internet().emailAddress());

        assertThatExceptionOfType(RuntimeException.class).isThrownBy(() -> userModel.getRelation("friends",
                                                                                                 UserModel.class));
    }
}
