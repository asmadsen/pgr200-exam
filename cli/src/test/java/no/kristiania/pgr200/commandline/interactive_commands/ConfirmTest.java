package no.kristiania.pgr200.commandline.interactive_commands;

import org.junit.Ignore;
import org.junit.Test;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class ConfirmTest {
    @Test @Ignore
    public void shouldReturnTrueIfYesLike() {
        InteractiveCommand command = mock(InteractiveCommand.class);

        Confirm prompt = new Confirm("setTopic", "Do you want to specify topic?", false);

        prompt.prompt(command);

        verify(command).write(eq("Do you want to specify topic? [y/N]"));

        verify(command).setValue("setTopic", true);
    }

    @Test @Ignore
    public void shouldReturnFalseIfNoLike() {
        InteractiveCommand command = mock(InteractiveCommand.class);

        Confirm prompt = new Confirm("setTopic", "Do you want to specify topic?", true);

        prompt.prompt(command);

        verify(command).write(eq("Do you want to specify topic? [Y/n]"));

        verify(command).setValue("setTopic", false);
    }
}
