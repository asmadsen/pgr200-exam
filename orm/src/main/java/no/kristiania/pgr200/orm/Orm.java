package no.kristiania.pgr200.orm;

import java.sql.Connection;

public class Orm {

    public static String quote = "`";
    public static Connection connection;

    public void setDatasource(Connection connection){
        Orm.connection = connection;
    }
}
