package no.kristiania.pgr200.server;

import no.kristiania.pgr200.common.http.HttpParser;
import no.kristiania.pgr200.common.http.HttpRequest;
import no.kristiania.pgr200.common.http.HttpResponse;
import no.kristiania.pgr200.common.http.HttpStatus;
import no.kristiania.pgr200.orm.Orm;
import no.kristiania.pgr200.server.db.DatabaseHandling;
import org.flywaydb.core.Flyway;
import org.slf4j.Logger;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.SQLException;
import java.util.Properties;

import static org.slf4j.LoggerFactory.getLogger;

public class ConferenceServer implements Runnable {
    private static Logger logger = getLogger(ConferenceServer.class);
    private Socket connection;
    private static final int PORT = 8080;
    public static Properties properties;

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

    public static void main(String[] args) {
        Flyway flyway = new Flyway();
        properties = new Properties();
        try {
            properties.load(new FileInputStream("innlevering.properties"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        flyway.setDataSource(properties.getProperty("dataSource.url"),
                             properties.getProperty("dataSource.username"),
                             properties.getProperty("dataSource.password"));
        flyway.setSchemas("conference_server");
        flyway.setLocations("filesystem:server/src/main/resources/db/migration");
//        flyway.clean();
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
            logger.info(String.format("%s %s %s\r\n",
                                      httpRequest.getHttpMethod().name(),
                                      httpRequest.getUri(),
                                      httpRequest.getHttpVersion()));
            RequestHandler requestHandler = new RequestHandler(httpRequest);
            if (httpRequest.getUri() != null || httpRequest.getHttpMethod() != null) {
                if (!httpRequest.getUri().startsWith("/api")) {
                    new HttpResponse(HttpStatus.NotFound).writeToStream(out);
                } else {
                    httpRequest.setUri(httpRequest.getUri().replaceFirst("/api", ""));
                    requestHandler.processRequest(out);
                }
            }
            connection.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
