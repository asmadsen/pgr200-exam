package no.kristiania.pgr200.commandline.interactive_commands;

import no.kristiania.pgr200.common.models.Talk;
import org.junit.Test;
import org.mockito.InOrder;

import java.util.LinkedList;

import static no.kristiania.pgr200.commandline.interactive_commands.Prompt.confirm;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.*;

public class PromptCreationTest {
    @Test
    public void shouldBeAbleToCreateConfirm() {
        PromptCreation creation = getSpy();

        creation.confirm("test", "test", false);

        verify(creation).addPrompt(isA(Confirm.class));
    }

    @Test
    public void shouldBeAbleToCreateInput() {
        PromptCreation creation = getSpy();

        creation.input("test", "test");
        creation.input("talk", "test", Talk.class);

        verify(creation, times(2)).addPrompt(isA(Input.class));
    }

    @Test
    public void shouldBeAbleToCreateListInput() {
        PromptCreation creation = getSpy();

        creation.list("test", "test", new String[]{"he", "ho", "ha"});

        verify(creation).addPrompt(isA(ListPrompt.class));
    }

    @Test
    public void shouldBeAbleToCreateConditionalPrompt() {
        PromptCreation creation = getSpy();

        creation.conditional(command -> false, confirm("confirm", "Confirm", false));

        creation.conditional(command -> false, new Confirm("confirm", "Confirm", false));

        verify(creation, times(2)).addPrompt(isA(ConditionalPrompt.class));
    }

    @Test
    public void shouldAddPromptsToGroupPromptInCorrectOrder() {
        PromptCreation creation = new PromptCreation();

        LinkedList<AbstractPrompt> list = new LinkedList<>();

        for (int i = 0; i < 100; i++) {
            int finalI = i;
            AbstractPrompt mock = spy(new AbstractPrompt(null) {
                @Override
                public void prompt(InteractiveCommand command) {
                    command.setValue("n" + finalI, finalI);
                }
            });
            list.push(mock);
            creation.addPrompt(mock);
        }

        InteractiveCommand command = Mocks.getInteractiveCommand();

        command.pushPrompt(creation.getPromptsAsPrompt());

        command.prompt();

        InOrder inOrder = inOrder(command);

        for (int i = 0; i < 100; i++) {
            inOrder.verify(command).setValue(eq("n" + i), eq(i));
        }
    }

    private PromptCreation getSpy() {
        return spy(new PromptCreation());
    }
}
