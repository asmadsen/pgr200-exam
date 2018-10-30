package no.kristiania.pgr200.commandline;

import com.github.javafaker.Faker;
import no.kristiania.pgr200.commandline.exceptions.DecodeCommandException;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;


public class ConferenceCliClientTest {

    @Rule
    public ExpectedException exceptionRule = ExpectedException.none();
    private Faker faker = new Faker();
    private ConferenceCliClient client = new ConferenceCliClient();

    @Test
    public void shouldNotFindCommandIfNotRegistered() {
        String title = faker.book().title();
        String description = faker.lorem().paragraph();
        exceptionRule.expect(DecodeCommandException.class);
        client.decodeCommand(new String[]{"add", "-description", description, "-title", title});
    }

}
