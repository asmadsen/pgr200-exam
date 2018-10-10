package no.kristiania.pgr200.server;

import no.kristiania.pgr200.common.Http.HttpParser;
import no.kristiania.pgr200.common.Http.HttpRequest;
import org.flywaydb.core.Flyway;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class ConferenceServer implements Runnable {
    private Socket connection;
    private static final int PORT = 8080;
    public static String DATASOURCE = "jdbc:mysql://localhost/conference_server";
    public static String USER = "root";
    public static String PASSWORD = "testkake";

    public ConferenceServer(Socket socket) {
        this.connection = socket;
    }

    public static void main(String[] args) {
        Flyway flyway = new Flyway();
        flyway.setDataSource("jdbc:mysql://localhost", "root", "testkake");
        flyway.setSchemas("conference_server");
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
