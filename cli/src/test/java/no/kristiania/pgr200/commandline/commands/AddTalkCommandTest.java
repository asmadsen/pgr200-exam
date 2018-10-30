package no.kristiania.pgr200.commandline.commands;

import com.github.javafaker.Faker;
import no.kristiania.pgr200.commandline.ConferenceCliClient;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class AddTalkCommandTest {
    private Faker faker = new Faker();

    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private final ByteArrayOutputStream errContent = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;
    private final PrintStream originalErr = System.err;

    @Before
    public void setUpStreams() {
        //System.setOut(new PrintStream(outContent));
        //System.setErr(new PrintStream(errContent));
    }

    @After
    public void restoreStreams() {
        //System.setOut(originalOut);
        //System.setErr(originalErr);
    }

    @Test
    public void shouldRegisterCommand() {
        ConferenceCliClient client = mock(ConferenceCliClient.class);
        AddTalkCommand.register(client);
        verify(client).register("add", AddTalkCommand.class);
    }
    /*
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

    @Test @Ignore
    public void shouldShowReceiptWhenDone() {
        String title = faker.book().title();
        String description = faker.lorem().paragraph(2);
        HttpClient client = mock(HttpClient.class);
        AddTalkCommand command = (AddTalkCommand) (new AddTalkCommand()).withHttpClient(client);
        CommandOptions options = new CommandOptions(new String[]{"-title", title, "-description", description});
        int id = faker.number().randomDigit();
        command.execute(options);
        Assert.assertEquals(String.format("Talk added and has id: #%d", id), outContent.toString());
    }
    */
}