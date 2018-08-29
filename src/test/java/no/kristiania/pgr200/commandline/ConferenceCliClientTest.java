package no.kristiania.pgr200.commandline;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalDate;
import java.util.UUID;

import com.github.javafaker.Faker;
import org.junit.Test;


public class ConferenceCliClientTest {

    private ConferenceCliClient client = new ConferenceCliClient();

    @Test
    public void shouldDecodeAddCommand() {
        Faker faker = new Faker();
        String title = faker.book().title();
        String description = faker.lorem().paragraph();
        ConferenceClientCommand command = client.decodeCommand(new String[] { "add", "-title", title, "-description", description });
        assertThat(command).isInstanceOf(AddTalkCommand.class)
            .isEqualToComparingFieldByField(new AddTalkCommand().withTitle(title).withDescription(description));
    }

}
