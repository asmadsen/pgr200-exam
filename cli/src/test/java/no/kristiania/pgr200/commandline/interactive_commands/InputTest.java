package no.kristiania.pgr200.commandline.interactive_commands;

import com.github.javafaker.Faker;
import no.kristiania.pgr200.common.models.Talk;
import org.junit.Test;

import static org.fusesource.jansi.Ansi.ansi;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

public class InputTest {
    Faker faker = new Faker();

    @Test
    public void shouldPromptUsers() {
        InteractiveCommand command = Mocks.getInteractiveCommand();
        String name = faker.funnyName().name();

        Input prompt = new Input("name", "What is your name?");

        when(command.getLineReader().readLine(eq("> "))).thenReturn(name);

        prompt.prompt(command);

        verify(command.getOutput()).println(eq("What is your name? "));
        verify(command).setValue(eq("name"), eq(name));
    }

    @Test
    public void shouldValidateInput() {
        InteractiveCommand command = Mocks.getInteractiveCommand();
        String name = faker.funnyName().name();

        Input<Talk> prompt = new Input<>("title", "What is your name?", Talk.class);

        when(command.getLineReader().readLine(eq("> ")))
                .thenReturn("")
                .thenReturn(name);

        prompt.prompt(command);

        verify(command.getOutput(), times(2)).println(eq("What is your name? "));
        verify(command).setValue(eq("title"), eq(name));
    }

    @Test
    public void shouldHaveDefaultValue() {
        InteractiveCommand command = Mocks.getInteractiveCommand();

        Input<Talk> prompt = new Input<>("title", "What is your name?", "John Doe", Talk.class);

        when(command.getLineReader().readLine(eq("> ")))
                .thenReturn("");

        prompt.prompt(command);

        verify(command.getOutput(), times(1)).println(eq("What is your name? " + ansi().fgBrightBlack()
                                                                                       .a("John Doe")
                                                                                       .reset()));
        verify(command).setValue(eq("title"), eq("John Doe"));
    }
}
