package no.kristiania.pgr200.server;

import com.github.javafaker.Faker;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import no.kristiania.pgr200.common.Http.HttpResponse;
import org.assertj.core.api.Java6Assertions;
import org.junit.*;
import org.junit.rules.ExpectedException;

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
        ConferenceServer.DATABASE_URL = "command_line_parser_test";
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
        JsonElement response = talkResponse.createTalk(new Talk(title, description));
        assertThat(response.getAsJsonObject().get("id")).isNotNull();
    }

    @Test
    public void shouldReturnNullIfTalkIsNull() throws SQLException {
        assertNull(talkResponse.createTalk(null));
    }

    @Rule
    public ExpectedException expectedEx = ExpectedException.none();

    @Ignore
    public void shouldThrowSQLException() throws SQLException {
        talkResponse.fetchTalkById(null);
        expectedEx.expectMessage("No value specified for parameter 1");
    }

}
