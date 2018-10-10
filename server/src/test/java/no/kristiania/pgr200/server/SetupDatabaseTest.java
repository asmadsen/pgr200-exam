package no.kristiania.pgr200.server;

import org.flywaydb.core.Flyway;

public class SetupDatabaseTest {

    public static void initSchema() {
        Flyway flyway = new Flyway();
        flyway.setDataSource("jdbc:h2:mem:conference_server;DB_CLOSE_DELAY=-1", "sa", "sa");
        flyway.clean();
        flyway.migrate();
    }
}
