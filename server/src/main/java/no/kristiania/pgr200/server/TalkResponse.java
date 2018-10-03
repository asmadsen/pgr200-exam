package no.kristiania.pgr200.server;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import no.kristiania.pgr200.server.db.DatabaseHandling;
import no.kristiania.pgr200.server.models.Talk;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

// Will be moved to Records.
@Deprecated
public class TalkResponse {

    private ArrayList<Talk> values;
    private Talk value;

    public TalkResponse(){
        values = new ArrayList<>();
        value = new Talk();
    }

    public ArrayList<Talk> getTalks(){
        return values;
    }

    public Talk getTalk() {
        return value;
    }

    public JsonElement createTalk(Talk talk) throws SQLException {
        if(talk != null){
            String statement = "INSERT INTO talks(title, description) VALUES (?, ?)";
            PreparedStatement preparedStatement = DatabaseHandling.getConnection().prepareStatement(statement);
            preparedStatement.setString(1, talk.getTitle());
            preparedStatement.setString(2, talk.getDescription());
            DatabaseHandling.executeStatement(preparedStatement);
            ResultSet newTalk = DatabaseHandling.selectStatement(
                    DatabaseHandling.getConnection().prepareStatement("SELECT id FROM talks ORDER BY id DESC LIMIT 1"));
            if(newTalk.next()){
                JsonObject jsonObject = new JsonObject();
                jsonObject.addProperty("id", newTalk.getString("id"));
                jsonObject.addProperty("url", "http://localhost:8080/api/talks/" + newTalk.getString("id"));
                return jsonObject;
            }
        }
        return null;
    }
}
