package no.kristiania.pgr200.commandline.Commands;

import com.github.javafaker.Faker;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import no.kristiania.pgr200.commandline.CommandOptions;
import no.kristiania.pgr200.commandline.ConferenceCliClient;
import no.kristiania.pgr200.commandline.Http.HttpClient;
import org.junit.Test;
import org.mockito.Mockito;

import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class AddTalkCommandTest {
    private Faker faker = new Faker();

    @Test
    public void shouldRegisterCommand() {
        ConferenceCliClient client = mock(ConferenceCliClient.class);
        AddTalkCommand.register(client);
        verify(client).register("add", AddTalkCommand.class);
    }

    @Test
    public void shouldCallHttpClient() {
        String title = faker.book().title();
        String description = faker.lorem().paragraph(2);
        HttpClient client = mock(HttpClient.class);
        AddTalkCommand command = (AddTalkCommand) (new AddTalkCommand()).withHttpClient(client);
        CommandOptions options = new CommandOptions(new String[]{"-title", title, "-description", description});
        command.execute(options);
        JsonObject object = new JsonObject();
        object.addProperty("title", title);
        object.addProperty("description", description);
        verify(client).post("/api/talks", object);
    }
}