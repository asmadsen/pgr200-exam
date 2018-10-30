package no.kristiania.pgr200.orm;

import java.sql.Connection;

public class Orm {

    public static String quote = "`";
    public static Connection connection;

    public static String QuoteString(String value) {
        return Orm.quote + value + Orm.quote;
    }

    public void setDatasource(Connection connection) {
        Orm.connection = connection;
    }
}
