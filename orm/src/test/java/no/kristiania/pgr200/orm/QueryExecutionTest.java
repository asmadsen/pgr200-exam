package no.kristiania.pgr200.orm;
import com.github.javafaker.Faker;
import no.kristiania.pgr200.orm.TestData.User;
import no.kristiania.pgr200.orm.TestData.UserModel;
import org.flywaydb.core.Flyway;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class QueryExecutionTest {

    UserModel model;

    @Before
    public void beforeEach(){
        model = Mockito.spy(new UserModel(new Faker().name().fullName(), new Faker().internet().emailAddress()));
    }

    @Test
    public void shouldFetchAllRecords() throws SQLException {
        setupDatabase();
        for (int i = 0; i < 10; i++) {
            UserModel userModel = new UserModel(new Faker().name().fullName(), new Faker().internet().emailAddress());
            userModel.save();
        }
        List<User> list = new UserModel().all();
        assertEquals(10, list.size());
    }

    @Test
    public void shouldInsertWhenNotExisting() throws SQLException {
        setupDatabase();
        when(model.save()).thenCallRealMethod();
        verify(model, times(1)).save();
        verify(model, times(1)).insertStatement();
    }

    @Test
    public void shouldUpdateWhenUuidIsPresentAndExisting() throws SQLException {
        setupDatabase();
        model.save();
        model.getState().setName(new Faker().name().fullName());
        when(model.save()).thenCallRealMethod();
        verify(model, times(1)).updateStatement();
    }

    @Test
    public void shouldNotChangeDbState() throws SQLException {
        setupDatabase();
        when(model.save()).thenCallRealMethod();
        when(model.isDirty()).thenCallRealMethod();
        when(model.save()).thenCallRealMethod();
        verify(model, times(1)).insertStatement();
        assertEquals(model.getState(), model.getDbState());
        verify(model, times(0)).updateStatement();
    }

    @Test
    public void shouldChangeDbState() throws SQLException {
        setupDatabase();
        when(model.save()).thenCallRealMethod();
        User oldDbState = model.getDbState();
        model.getState().setName(new Faker().name().fullName());
        when(model.save()).thenCallRealMethod();
        verify(model, times(1)).updateStatement();
        verify(model, times(1)).insertStatement();
        assertNotEquals(0, oldDbState.compareTo(model.getDbState()));
    }

    @Test
    public void shouldBuildInsert() {
        assertEquals("INSERT INTO `users` (`name`, `id`, `email`) VALUES (?, ?, ?);", model.insertStatement());
    }

    @Test
    public void shouldBuildUpdate() {
         assertEquals("UPDATE `users` SET `name` = ?, `email` = ? WHERE `id` = ?;", model.updateStatement());
    }

    private void setupDatabase() throws SQLException {
        Flyway flyway = new Flyway();
        flyway.setDataSource("jdbc:h2:mem:conference_server;DB_CLOSE_DELAY=-1", "sa", "sa");
        flyway.clean();
        flyway.migrate();
        DatabaseConnection.setConnection(DriverManager.getConnection(
                "jdbc:h2:mem:conference_server;DB_CLOSE_DELAY=-1", "sa", "sa"));
    }
}
