package no.kristiania.pgr200.server;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class TalkResponse {

    private ArrayList<Talk> Values;
    private Talk Value;

    public TalkResponse(){
        Values = new ArrayList<>();
        Value = new Talk();
    }

    public ArrayList<Talk> getTalks(){
        return Values;
    }

    public Talk getTalk() {
        return Value;
    }

    public void fetchAllTalks() throws SQLException {
        String statement = "SELECT id, title, description FROM talks";
        queryTalks(statement, null);
    }

    public Talk fetchTalkById(String id) throws SQLException {
        String statement = "SELECT id, title, description FROM talks WHERE id = ? LIMIT 1";
        ResultSet talkResult = queryTalks(statement, id);
        while(talkResult.next()){
            Value.withId(talkResult.getString("id"))
                    .withTitle(talkResult.getString("title"))
                    .withDescription(talkResult.getString("description"));
        }
        return Value;
    }

    public String createTalk(Talk talk) throws SQLException {
        if(talk != null){
            String statement = "INSERT INTO talks(title, description) VALUES (?, ?)";
            PreparedStatement preparedStatement = DatabaseHandling.getConnection().prepareStatement(statement);
            preparedStatement.setString(1, talk.getTitle());
            preparedStatement.setString(2, talk.getDescription());
            DatabaseHandling.executeStatement(preparedStatement);
            ResultSet newTalk = DatabaseHandling.selectStatement(
                    DatabaseHandling.getConnection().prepareStatement("SELECT id FROM talks ORDER BY id DESC LIMIT 1"));
            if(newTalk.next()) return "{\"Values\": { \"id\": \"" +
                    newTalk.getString("id") + "\",\"url\": \"http://localhost:8080/api/talks/" +
                    newTalk.getString("id") + "\"}}";
        }
        return null;
    }

    public ResultSet queryTalks(String statement, String id) throws SQLException {
        PreparedStatement preparedStatement = DatabaseHandling.getConnection().prepareStatement(statement);
        if (id != null) preparedStatement.setString(1, id);
//        ResultSet talkResult =
//        while (talkResult.next()) {
//            Values.add(new Talk(
//                    talkResult.getString("id"),
//                    talkResult.getString("title"),
//                    talkResult.getString("description")));
//        }
        return DatabaseHandling.selectStatement(preparedStatement);
    }
}
