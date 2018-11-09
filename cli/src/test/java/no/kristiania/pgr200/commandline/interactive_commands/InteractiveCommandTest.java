package no.kristiania.pgr200.commandline.interactive_commands;

import org.junit.Test;
import org.mockito.InOrder;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.mock;

public class InteractiveCommandTest {
    @Test
    public void shouldCallAllPrompts() {
        InteractiveCommand command = Mocks.getInteractiveCommand();

        AbstractPrompt mock1 = mock(AbstractPrompt.class);
        AbstractPrompt mock2 = mock(AbstractPrompt.class);
        AbstractPrompt mock3 = mock(AbstractPrompt.class);

        command.pushPrompt(mock1);
        command.pushPrompt(mock2);
        command.pushPrompt(mock3);

        command.prompt();

        InOrder inOrder = inOrder(mock1, mock2, mock3);

        inOrder.verify(mock1).prompt(eq(command));
        inOrder.verify(mock2).prompt(eq(command));
        inOrder.verify(mock3).prompt(eq(command));
    }
}