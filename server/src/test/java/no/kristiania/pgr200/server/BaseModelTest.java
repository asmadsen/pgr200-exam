package no.kristiania.pgr200.server;

import com.github.javafaker.Faker;
import no.kristiania.pgr200.common.Models.Talk;
import no.kristiania.pgr200.server.models.TalkModel;
import org.junit.*;

import java.util.List;

import static org.junit.Assert.assertEquals;

public class BaseModelTest {

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

    @Ignore
    @Test
    public void shouldReturnTalksList() throws Exception {
        new TalkModel(getFakeTitle(), getFakeDescription(12));
        List<Talk> talks = new TalkModel().all();
        assertEquals(1, talks.size());
    }
}
