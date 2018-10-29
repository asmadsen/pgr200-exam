package no.kristiania.pgr200.orm;

import org.flywaydb.core.Flyway;

import java.sql.DriverManager;
import java.sql.SQLException;

public class TestUtils {
    public static void setupDatabase() throws SQLException {
        Flyway flyway = new Flyway();
        flyway.setDataSource("jdbc:h2:mem:conference_server;DB_CLOSE_DELAY=-1", "sa", "sa");
        flyway.clean();
        flyway.migrate();
        new Orm().setDatasource(DriverManager.getConnection(
                "jdbc:h2:mem:conference_server;DB_CLOSE_DELAY=-1", "sa", "sa"));
    }
}
