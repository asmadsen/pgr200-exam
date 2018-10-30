package no.kristiania.pgr200.orm;
import com.github.javafaker.Faker;
import no.kristiania.pgr200.orm.TestData.User;
import no.kristiania.pgr200.orm.TestData.UserModel;
import org.apache.commons.lang3.SerializationUtils;
import org.flywaydb.core.Flyway;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.mockito.Mockito.*;

public class QueryExecutionTest {

    UserModel model;

    @Before
    public void beforeEach(){
        model = Mockito.spy(new UserModel(new Faker().name().fullName(), new Faker().internet().emailAddress()));
    }

    @Test
    public void shouldDestroyRecord() throws SQLException {
        setupDatabase();
        model.save();
        UUID id = model.getState().getId();
        UserModel user = new UserModel().findById(id);
        assertThat(user).isNotNull();
        model.destroy();
        user = new UserModel().findById(id);
        assertThat(user).isNull();
    }

    @Test
    public void shouldFindRecordById() throws SQLException {
         setupDatabase();
         model.save();
         UserModel user = new UserModel().findById(model.getState().getId());
         assertThat(user.getState())
                 .hasFieldOrPropertyWithValue("id", model.getState().getId())
                 .hasFieldOrPropertyWithValue("name", model.getState().getName())
                 .hasFieldOrPropertyWithValue("email", model.getState().getEmail());
    }

    @Test
    public void shouldReturnNullIfNotExisting() throws SQLException {
         setupDatabase();
         UserModel user = new UserModel().findById(UUID.randomUUID());
         assertThat(user).isNull();
    }

    @Test
    public void shouldFetchAllRecords() throws SQLException {
        setupDatabase();
        for (int i = 0; i < 10; i++) {
            UserModel userModel = new UserModel(new Faker().name().fullName(), new Faker().internet().emailAddress());
            userModel.save();
        }
        List<UserModel> list = new UserModel().all();
        assertEquals(10, list.size());
    }

    @Test
    public void shouldInsertWhenNotExisting() throws SQLException {
        setupDatabase();
        when(model.save()).thenCallRealMethod();
        verify(model, times(1)).save();
    }

    @Test
    public void shouldUpdateWhenUuidIsPresentAndExisting() throws SQLException {
        setupDatabase();
        model.save();
        model.getState().setName(new Faker().name().fullName());
        when(model.save()).thenCallRealMethod();
    }

    @Test
    public void shouldNotChangeDbState() throws SQLException {
        setupDatabase();
        when(model.save()).thenCallRealMethod();
        when(model.isDirty()).thenCallRealMethod();
        when(model.save()).thenCallRealMethod();
        assertEquals(model.getState(), model.getDbState());
    }

    @Test
    public void shouldCreateWithFill() throws SQLException {
        setupDatabase();
        Map<String, ColumnValue> attributes = new HashMap<>();
        attributes.put("name", new ColumnValue<>("John Doe"));
        attributes.put("email", new ColumnValue<>("john@example.com"));

        new UserModel().create(attributes);
        List<UserModel> user = new UserModel().all();
        assertThat(user.size()).isEqualTo(1);
        assertThat(user.get(0).getState())
                .hasFieldOrPropertyWithValue("name", "John Doe")
                .hasFieldOrPropertyWithValue("email", "john@example.com");
    }

    @Test
    public void shouldFillModelState(){
        Map<String, ColumnValue> attributes = new HashMap<>();
        attributes.put("name", new ColumnValue<>("John Doe"));
        attributes.put("email", new ColumnValue<>("john@example.com"));

        UserModel model = new UserModel();
        model.fill(attributes);
        assertThat(model.getState())
                .hasFieldOrPropertyWithValue("name", "John Doe")
                .hasFieldOrPropertyWithValue("email", "john@example.com");
    }

    @Test
    public void shouldChangeDbState() throws SQLException {
        setupDatabase();
        when(model.save()).thenCallRealMethod();
        User oldDbState = model.newStateInstance().withAttributes(model.getDbState().getAttributes());
        model.getState().setName(new Faker().name().fullName());
        when(model.save()).thenCallRealMethod();
        assertNotEquals(oldDbState, model.getDbState());
    }

    @Test
    public void shouldGetResults() throws SQLException {
        setupDatabase();
        UserModel model = new UserModel("John Doe", "doe@example.com");
        model.save();
        SelectQuery query = new SelectQuery<>(model).count("id").groupBy("id");
        List<User> results = query.get();
        assertEquals(1, results.size());
    }

    @Test
    public void shouldReturnFalseWhenRecordNotFound() throws SQLException {
        setupDatabase();
        assertThat(model.destroy()).isFalse();
        verify(model, times(1)).destroy();
    }

    private void setupDatabase() throws SQLException {
        Flyway flyway = new Flyway();
        flyway.setDataSource("jdbc:h2:mem:conference_server;DB_CLOSE_DELAY=-1", "sa", "sa");
        flyway.clean();
        flyway.migrate();
        new Orm().setDatasource(DriverManager.getConnection(
                "jdbc:h2:mem:conference_server;DB_CLOSE_DELAY=-1", "sa", "sa"));
    }
}
