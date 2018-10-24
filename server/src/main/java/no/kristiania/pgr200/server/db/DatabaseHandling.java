package no.kristiania.pgr200.server.db;

import no.kristiania.pgr200.server.ConferenceServer;

import java.sql.*;

public class DatabaseHandling {
    public static Connection con;

    public Connection createConnection() throws SQLException {
        return DriverManager.getConnection(
                ConferenceServer.DATASOURCE,
                ConferenceServer.USER,
                ConferenceServer.PASSWORD);
    }

    public static Connection getConnection() throws SQLException {
        if(con == null){
            DatabaseHandling db = new DatabaseHandling();
            con = db.createConnection();
        }
        return con;
    }
}
