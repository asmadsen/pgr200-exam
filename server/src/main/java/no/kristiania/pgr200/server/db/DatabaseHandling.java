package no.kristiania.pgr200.server.db;

import no.kristiania.pgr200.server.ConferenceServer;

import java.sql.*;

public class DatabaseHandling {
    public static Connection con;

    public Connection createConnection() throws SQLException {
        return DriverManager.getConnection(
                ConferenceServer.properties.getProperty("dataSource.url"),
                ConferenceServer.properties.getProperty("dataSource.username"),
                ConferenceServer.properties.getProperty("dataSource.password"));
    }

    public static Connection getConnection() throws SQLException {
        if(con == null){
            DatabaseHandling db = new DatabaseHandling();
            con = db.createConnection();
        }
        return con;
    }
}
