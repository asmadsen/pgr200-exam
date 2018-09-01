package no.kristiania.pgr200.commandline;

import com.github.javafaker.Faker;
import no.kristiania.pgr200.commandline.Commands.AddTalkCommand;
import no.kristiania.pgr200.commandline.Commands.ConferenceClientCommand;
import no.kristiania.pgr200.commandline.Exceptions.DecodeCommandException;
import no.kristiania.pgr200.commandline.Http.HttpClient;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;


public class ConferenceCliClientTest {

    @Rule
    public ExpectedException exceptionRule = ExpectedException.none();
    private Faker faker = new Faker();
    private HttpClient httpClient = mock(HttpClient.class);
    private ConferenceCliClient client = new ConferenceCliClient(httpClient);

    @Test
    public void shouldDecodeAddCommand() {
        AddTalkCommand.register(client);
        String title = faker.book().title();
        String description = faker.lorem().paragraph();
        ConferenceClientCommand command = client.decodeCommand(new String[]{"add", "-title", title, "-description", description});
        assertThat(command).isInstanceOf(AddTalkCommand.class)
                .isEqualToComparingFieldByField(new AddTalkCommand().withTitle(title).withDescription(description).withHttpClient(httpClient));
    }

    @Test
    public void shouldDecodeAddCommandWithOptionsInOppositeSequence() {
        AddTalkCommand.register(client);
        String title = faker.book().title();
        String description = faker.lorem().paragraph();
        ConferenceClientCommand command = client.decodeCommand(new String[]{"add", "-description", description, "-title", title});
        assertThat(command).isInstanceOf(AddTalkCommand.class)
                .isEqualToComparingFieldByField(new AddTalkCommand().withTitle(title).withDescription(description).withHttpClient(httpClient));
    }

    @Test
    public void shouldNotFindCommandIfNotRegistered() {
        String title = faker.book().title();
        String description = faker.lorem().paragraph();
        exceptionRule.expect(DecodeCommandException.class);
        client.decodeCommand(new String[]{"add", "-description", description, "-title", title});
    }

}
