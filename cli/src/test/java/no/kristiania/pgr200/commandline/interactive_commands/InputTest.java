package no.kristiania.pgr200.commandline.interactive_commands;

import com.github.javafaker.Faker;
import org.junit.Test;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class InputTest {
    Faker faker = new Faker();

    @Test
    public void shouldPromptUsers() {
        InteractiveCommand command = mock(InteractiveCommand.class);
        String name = faker.funnyName().name();

        Input prompt = new Input("name", "What is your name?");

        when(command.readNextLine()).thenReturn(name);

        prompt.prompt(command);

        verify(command).write(eq("What is your name? "));
        verify(command).setValue(eq("name"), eq(name));
    }
}
