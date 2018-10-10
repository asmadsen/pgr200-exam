package no.kristiania.pgr200.server;

import com.github.javafaker.Faker;
import no.kristiania.pgr200.server.models.Records;
import no.kristiania.pgr200.server.models.Talk;
import org.junit.*;

import java.sql.SQLException;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class RecordsTest {

    private TalkResponse talkResponse = new TalkResponse();

    private String getFakeTitle(){
        return new Faker().book().title();
    }

    private String getFakeDescription(int words){
        return new Faker().lorem().sentence(words);
    }

    @Before
    public void init(){
        ConferenceServer.DATASOURCE = "jdbc:h2:mem:conference_server;DB_CLOSE_DELAY=-1";
        ConferenceServer.USER = "sa";
        ConferenceServer.PASSWORD = "sa";
        SetupDatabaseTest.initSchema();
    }

    @Test
    public void shouldReturnTalksList() throws SQLException {
        talkResponse.createTalk(new Talk(getFakeTitle(), getFakeDescription(12)));
        List<Talk> talks = new Talk().query("talks", "id", "title", "description");
        assertEquals(1, talks.size());
    }

    @Test
    public void shouldBuildStatement(){
        Records records = new Records();
        StringBuilder statement = records.buildSelectStatement("tableName", "name", "surname");
        assertEquals(statement.toString(), "SELECT name, surname FROM tableName;");
    }

    @Test
    public void shouldReturnNullIfTalkIsNull() throws SQLException {
        assertNull(talkResponse.createTalk(null));
    }
}
