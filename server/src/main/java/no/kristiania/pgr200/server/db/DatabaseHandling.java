package no.kristiania.pgr200.server.db;

import no.kristiania.pgr200.server.ConferenceServer;

import java.sql.*;

public class DatabaseHandling {
    public static Connection con;
    private static ResultSet rs;

    public Connection createConnection() throws SQLException {
        Connection connection = null;
        connection = DriverManager.getConnection("jdbc:mysql://localhost/" + ConferenceServer.DATABASE_URL + "?serverTimezone=UTC", "root", "testkake");
        return connection;
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
