package no.kristiania.pgr200.server;

import com.github.javafaker.Faker;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import org.junit.*;

import java.sql.SQLException;

import static org.assertj.core.api.Java6Assertions.assertThat;
import static org.junit.Assert.assertNull;

public class TalkResponseTest {

    private TalkResponse talkResponse = new TalkResponse();

    private String getFakeTitle(){
        return new Faker().book().title();
    }

    private String getFakeDescription(int words){
        return new Faker().lorem().sentence(words);
    }

    @Before
    public void init(){
        ConferenceServer.DATABASE_URL = "conference_server_test";
        SetupDatabaseTest.initSchema();
    }

    @Test
    public void shouldReturnAllTalks() throws SQLException {
        talkResponse.createTalk(new Talk(getFakeTitle(), getFakeDescription(12)));
        talkResponse.fetchAllTalks();
        Assert.assertEquals(1, talkResponse.getTalks().size());
    }

    @Test
    public void shouldFetchTalk() throws SQLException {
        talkResponse.createTalk(new Talk(getFakeTitle(), getFakeDescription(12)));
        talkResponse.fetchTalkById("1");
        Assert.assertEquals(1, talkResponse.getTalks().size());
    }

    @Test
    public void shouldReturnATalkId() throws SQLException {
        String title = getFakeTitle();
        String description = getFakeDescription(12);
        String response = talkResponse.createTalk(new Talk(title, description));
        String id = new Gson().fromJson(response, JsonObject.class).getAsJsonObject("Values").get("id").toString();
        assertThat(id).isNotNull();
    }

    @Test
    public void shouldReturnNullIfTalkIsNull() throws SQLException {
        assertNull(talkResponse.createTalk(null));
    }
}
