package no.kristiania.pgr200.server;

import org.flywaydb.core.Flyway;

public class SetupDatabaseTest {

    public static void initSchema() {
        Flyway flyway = new Flyway();
        flyway.setDataSource("jdbc:mysql://localhost/command_line_parser_test?serverTimezone=UTC", "root", "testkake");
        flyway.clean();
        flyway.migrate();
    }
}
