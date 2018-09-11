package no.kristiania.pgr200.server;

import java.io.PrintWriter;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;

public class RequestHandler {

    HashMap<String, String> headers;
    String request, body;

    public RequestHandler(String request, HashMap<String, String> headers, String body){
        this.request = request;
        this.headers = headers;
        this.body = body;
    }

    public void processRequest(PrintWriter out) throws InvocationTargetException, IllegalAccessException, ClassNotFoundException, NoSuchMethodException {
        TalksController talksController = new TalksController();
        talksController.setOut(out);
        Method controllerMethod = talksController.getMethodFromAnnotation(request);
        if(controllerMethod != null) controllerMethod.invoke(talksController);
    }
//
//    private void processGET(PrintWriter out, StringTokenizer parse){
//        String token = parse.nextToken();
//        if(token.matches("/favicon[.]ico")){
////            sendFavIcon(out);
//        } else if (token.matches("/api/talks/(\\d+)")){
//            getTalkById(out, token);
//        } else if (token.matches("/api/talks/?")){
//            getAllTalks(out);
//        } else {
//            badRequest(out);
//        }
//    }
//
//    private void processPOST(BufferedReader in, PrintWriter out) throws IOException, SQLException {
//        StringBuilder body = new StringBuilder();
//        String inputLine;
//        String contentLengthHeader = "content-length: ";
//        int contentLength = 0;
//        while (!(inputLine = in.readLine()).equals("")){
//            if(inputLine.startsWith("content-length: ")){
//                contentLength = Integer.parseInt(inputLine.substring(contentLengthHeader.length()));
//            }
//        }
//        for (int i = 0; i < contentLength; i++){
//            body.append((char) in.read());
//        }
//        TalkResponse talkResponse = new TalkResponse();
//        Talk talk = new Gson().fromJson(body.toString(), Talk.class);
//        sendSuccessResponse(out, "application/json");
//        out.println(talkResponse.createTalk(talk));
//        System.out.println(talkResponse.createTalk(talk));
//    }
//
//    private void getAllTalks(PrintWriter out){
//        TalkResponse talkResponse = new TalkResponse();
//        talkResponse.fetchAllTalks();
//        if (talkResponse.getTalks().isEmpty()){
//            sendUnprocessableResponse(out);
//        } else {
//            Gson gson = new Gson();
//            String json = gson.toJson(talkResponse);
//            JsonObject obj = new JsonParser().parse(json).getAsJsonObject();
//            obj.get("Talks").getAsJsonArray().forEach(e -> e.getAsJsonObject().remove("description"));
//            sendTalks(out, gson.toJson(obj));
//        }
//    }
//
//    private void sendTalks(PrintWriter out, String talkResponse){
//
//    }
}
