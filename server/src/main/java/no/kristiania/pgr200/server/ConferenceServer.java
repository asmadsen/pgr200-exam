package no.kristiania.pgr200.server;

import no.kristiania.pgr200.common.Http.HttpParser;
import no.kristiania.pgr200.common.Http.HttpRequest;
import no.kristiania.pgr200.orm.Orm;
import no.kristiania.pgr200.server.db.DatabaseHandling;
import org.flywaydb.core.Flyway;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.SQLException;

public class ConferenceServer implements Runnable {
    private Socket connection;
    private static final int PORT = 8080;
    public static String DATASOURCE = "jdbc:postgresql://localhost/postgres";
    public static String USER = "postgres";
    public static String PASSWORD = "postgres";

    public ConferenceServer(Socket socket) {
        this.connection = socket;
        try {
            Orm.connection = DatabaseHandling.getConnection();
            Orm.quote = "\"";
            DatabaseHandling.getConnection().setSchema("conference_server");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws SQLException {
        Flyway flyway = new Flyway();
        flyway.setDataSource("jdbc:postgresql://localhost/postgres", "postgres", "postgres");
        flyway.setSchemas("conference_server");
        flyway.setLocations("filesystem:server/src/main/resources/db/migration");
        flyway.migrate();
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            while (true) {
                ConferenceServer conferenceServer = new ConferenceServer(serverSocket.accept());
                Thread thread = new Thread(conferenceServer);
                thread.start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override

    public void run() {
        try (InputStream in = connection.getInputStream(); OutputStream out = connection.getOutputStream()) {
            HttpRequest httpRequest = new HttpParser().parseRequest(in);
            RequestHandler requestHandler = new RequestHandler(httpRequest);
            if(httpRequest.getUri() != null || httpRequest.getHttpMethod() != null) {
                requestHandler.processRequest(out);
            }
            connection.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
