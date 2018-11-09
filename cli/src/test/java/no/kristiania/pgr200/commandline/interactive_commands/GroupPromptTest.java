package no.kristiania.pgr200.commandline.interactive_commands;

import org.junit.Test;
import org.mockito.InOrder;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.mock;

public class GroupPromptTest {
    @Test
    public void shouldRelayPromptsToSubPrompts() {
        InteractiveCommand command = Mocks.getInteractiveCommand();
        AbstractPrompt mock1 = mock(AbstractPrompt.class);
        AbstractPrompt mock2 = mock(AbstractPrompt.class);
        AbstractPrompt mock3 = mock(AbstractPrompt.class);

        GroupPrompt prompt = new GroupPrompt(
                mock1,
                mock2,
                mock3
        );

        prompt.prompt(command);

        InOrder inorder = inOrder(mock1, mock2, mock3);
        inorder.verify(mock1).prompt(eq(command));
        inorder.verify(mock2).prompt(eq(command));
        inorder.verify(mock3).prompt(eq(command));
    }
}
