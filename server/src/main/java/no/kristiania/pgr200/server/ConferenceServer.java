package no.kristiania.pgr200.server;

import org.flywaydb.core.Flyway;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.lang.reflect.InvocationTargetException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;

public class ConferenceServer implements Runnable {
    private Socket connection;
    private static final int PORT = 8080;
    public static String DATABASE_URL = "command_line_parser";

    public ConferenceServer(Socket socket) {
        this.connection = socket;
    }

    public static void main(String[] args) {
        Flyway flyway = new Flyway();
        flyway.setDataSource("jdbc:mysql://localhost/" + DATABASE_URL + "?serverTimezone=UTC", "kjell", "123456");
        flyway.migrate();
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            boolean listening = true;
            while (listening) {
                ConferenceServer conferenceServer = new ConferenceServer(serverSocket.accept());
                Thread thread = new Thread(conferenceServer);
                thread.start();
                if(DATABASE_URL.equals("command_line_parser_test")) listening = false;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        try (BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
             PrintWriter out = new PrintWriter(connection.getOutputStream())) {
            HashMap<String, String> headers = new HashMap<>();
            String request = in.readLine();
            if(request == null) return;
            String requestLine = request;
            while(!(request = in.readLine()).equals("")){
                String[] line = request.split(": ");
                headers.put(line[0], line[1]);
            }
            StringBuilder body = new StringBuilder();
            if(requestLine.toUpperCase().matches("POST")){
                int input;
                while((input = in.read()) != -1){
                    body.append((char) input);
                }
            }
            RequestHandler requestHandler = new RequestHandler(requestLine, headers, body.toString());
            requestHandler.processRequest(out);
            connection.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
