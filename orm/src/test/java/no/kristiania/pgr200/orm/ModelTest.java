package no.kristiania.pgr200.orm;
import com.github.javafaker.Faker;
import no.kristiania.pgr200.orm.TestData.UserModel;
import org.flywaydb.core.Flyway;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

public class ModelTest {

    UserModel model;

    @Before
    public void beforeEach(){
        model = Mockito.spy(new UserModel(new Faker().name().fullName(), new Faker().internet().emailAddress()));
    }

    @Test
    public void shouldInsertWhenNotExisting() throws SQLException {
        setupDatabase();
        when(model.save()).thenCallRealMethod();
        verify(model, times(1)).save();
        verify(model, times(1)).insertStatement();
    }

    @Test
    public void shouldInsertWhenUuidIsPresentAndNotExisting() throws SQLException {
        setupDatabase();
        model.model.setId(UUID.randomUUID());
        when(model.exists()).thenReturn(true);
        when(model.save()).thenCallRealMethod();
        verify(model, times(1)).updateStatement();
    }

    @Test
    public void shouldUpdateWhenUuidIsPresentAndExisting() throws SQLException {
        setupDatabase();
        model.model.setId(UUID.randomUUID());
        model.save();
        model.model.setName(new Faker().name().fullName());
        when(model.exists()).thenReturn(true);
        when(model.save()).thenCallRealMethod();
        verify(model, times(1)).updateStatement();
    }

    @Test
    public void shouldBuildInsert() {
        when(model.insertStatement()).thenCallRealMethod();
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
