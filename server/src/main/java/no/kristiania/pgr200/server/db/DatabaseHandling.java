package no.kristiania.pgr200.server.db;

import no.kristiania.pgr200.server.ConferenceServer;

import java.sql.*;

public class DatabaseHandling {
    public static Connection con;

    public Connection createConnection() throws SQLException {
        System.out.println(ConferenceServer.DATASOURCE);
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

    public static ResultSet selectStatement(PreparedStatement statement) throws SQLException {
        return statement.executeQuery();
    }

    public static void executeStatement(PreparedStatement statement) throws SQLException {
        statement.executeUpdate();
    }
}
