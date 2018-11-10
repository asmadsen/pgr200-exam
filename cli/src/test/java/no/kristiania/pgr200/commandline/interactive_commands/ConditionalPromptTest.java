package no.kristiania.pgr200.commandline.interactive_commands;

import org.junit.Test;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

public class ConditionalPromptTest {
    @Test
    public void shouldRunPromptPredicateIsTrue() {
        InteractiveCommand command = mock(InteractiveCommand.class);

        AbstractPrompt childPrompt = mock(AbstractPrompt.class);

        ConditionalPrompt prompt = new ConditionalPrompt(command1 -> true, childPrompt);

        prompt.prompt(command);

        verify(childPrompt).prompt(eq(command));
    }

    @Test
    public void shouldNotRunPromptPredicateIsFalse() {
        InteractiveCommand command = mock(InteractiveCommand.class);

        AbstractPrompt childPrompt = mock(AbstractPrompt.class);

        ConditionalPrompt prompt = new ConditionalPrompt(command1 -> false, childPrompt);

        prompt.prompt(command);

        verify(childPrompt, never()).prompt(eq(command));
    }
}