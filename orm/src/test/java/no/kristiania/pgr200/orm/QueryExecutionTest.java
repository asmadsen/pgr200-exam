package no.kristiania.pgr200.orm;

import org.flywaydb.core.Flyway;
import org.junit.Before;
import org.junit.Test;

public class QueryExecutionTest {

    @Before
    public void setupDb() {
        Flyway flyway = new Flyway();
        flyway.setDataSource("jdbc:h2:mem:conference_server;DB_CLOSE_DELAY=-1", "sa", "sa");
        flyway.clean();
        flyway.migrate();
    }
}
