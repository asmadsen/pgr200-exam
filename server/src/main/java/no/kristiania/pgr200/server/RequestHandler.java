package no.kristiania.pgr200.server;

import no.kristiania.pgr200.common.Http.HttpMethod;

import java.io.PrintWriter;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

class RequestHandler {

    private HashMap<String, String> headers;
    private String route, body;
    private HttpMethod httpMethod;
    private BaseController controller;

    RequestHandler(String requestLine, HashMap<String, String> headers, String body){
        this.httpMethod = HttpMethod.valueOf(requestLine.split(" ")[0]);
        this.route = requestLine.split(" ")[1];
        this.headers = headers;
        this.body = body;
    }

    void processRequest(PrintWriter out) throws InvocationTargetException, IllegalAccessException {
        BaseController controller = getControllerFromRoute();
        Method controllerMethod = null;
        if (controller != null) controllerMethod = getMethodFromAnnotation(controller);
        if(controllerMethod != null){
            controller.setOut(out);
            setController(controller);
            controllerMethod.invoke(controller);
        }
    }

    BaseController getControllerFromRoute(){
        for (Map.Entry<BaseController, ApiController> entry : getControllers().entrySet()) {
            Pattern regex = Pattern.compile(entry.getValue().value());
            Matcher matcher = regex.matcher(route);
            if(matcher.find()) return entry.getKey();
        }
        return null;
    }

    private HashMap<BaseController, ApiController> getControllers(){
        // TODO: Use enum for controller classes?
        HashMap<BaseController, ApiController> annotations = new HashMap<>();
        annotations.put(new TalksController(), TalksController.class.getAnnotation(ApiController.class));
        annotations.put(new ScheduleController(), ScheduleController.class.getAnnotation(ApiController.class));
        return annotations;
    }

    Method getMethodFromAnnotation(BaseController controller) {
        Method[] methods = controller.getClass().getDeclaredMethods();
        for (Method method : methods) {
            if (method.isAnnotationPresent(ApiRequest.class)) {
                ApiRequest annotation = method.getAnnotation(ApiRequest.class);
                if (route.split("/").length == annotation.route().split("/").length
                        && annotation.action().equals(httpMethod)
                        && Pattern.compile(annotation.route()).matcher(route).find()) {
                    controller.setMethod(httpMethod);
                    controller.setRoute(route);
                    return method;
                }
            }
        }
        return null;
    }

    public BaseController getController() {
        return controller;
    }

    public void setController(BaseController controller) {
        this.controller = controller;
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
