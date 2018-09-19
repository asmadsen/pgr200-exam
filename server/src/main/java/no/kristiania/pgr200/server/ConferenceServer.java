package no.kristiania.pgr200.server;

import no.kristiania.pgr200.common.Http.HttpMethod;
import no.kristiania.pgr200.common.Http.HttpParser;
import no.kristiania.pgr200.common.Http.HttpRequest;
import org.flywaydb.core.Flyway;

import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;

import static java.lang.System.in;

public class ConferenceServer implements Runnable {
    private Socket connection;
    private static final int PORT = 8080;
    public static String DATABASE_URL = "conference_server";

    public ConferenceServer(Socket socket) {
        this.connection = socket;
    }

    public static void main(String[] args) {
        Flyway flyway = new Flyway();
        flyway.setDataSource("jdbc:mysql://localhost", "root", "testkake");
        flyway.setSchemas(DATABASE_URL);
        flyway.migrate();
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            boolean listening = true;
            while (listening) {
                ConferenceServer conferenceServer = new ConferenceServer(serverSocket.accept());
                Thread thread = new Thread(conferenceServer);
                thread.start();
                if(DATABASE_URL.equals("conference_server_test")) listening = false;
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
