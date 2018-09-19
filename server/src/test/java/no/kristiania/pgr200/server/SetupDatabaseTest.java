package no.kristiania.pgr200.server;

import org.flywaydb.core.Flyway;

public class SetupDatabaseTest {

    public static void initSchema() {
        Flyway flyway = new Flyway();
        flyway.setDataSource("jdbc:mysql://localhost", "root", "testkake");
        flyway.setSchemas("conference_server_test");
        flyway.clean();
        flyway.migrate();
    }
}
