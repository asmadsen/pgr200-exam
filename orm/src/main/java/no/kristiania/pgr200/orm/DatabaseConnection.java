package no.kristiania.pgr200.orm;

import java.sql.Connection;

public class DatabaseConnection {
    public static Connection connection;

    public static void setConnection(Connection connection){
        DatabaseConnection.connection = connection;
    }
}
