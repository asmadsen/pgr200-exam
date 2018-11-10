package no.kristiania.pgr200.commandline.interactive_commands;

import org.jline.reader.LineReader;
import org.junit.Test;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.contains;
import static org.mockito.Mockito.*;

public class ConfirmTest {
    @Test
    public void shouldReturnTrueIfYesLike() {
        InteractiveCommand command = Mocks.getInteractiveCommand();

        LineReader lineReader = command.getLineReader();

        when(lineReader.readLine(anyString())).thenReturn("y");

        Confirm prompt = new Confirm("setTopic", "Do you want to specify topic?", false);

        prompt.prompt(command);

        verify(command.getTerminal().writer()).println(contains("Do you want to specify topic?"));

        when(lineReader.readLine(anyString())).thenReturn("yEs");
        prompt.prompt(command);

        when(lineReader.readLine(anyString())).thenReturn("YeS");
        prompt.prompt(command);

        when(lineReader.readLine(anyString())).thenReturn("Y");
        prompt.prompt(command);

        verify(command, times(4)).setValue("setTopic", true);
    }

    @Test
    public void shouldReturnFalseIfNoLike() {
        InteractiveCommand command = Mocks.getInteractiveCommand();

        LineReader lineReader = command.getLineReader();

        when(lineReader.readLine(anyString())).thenReturn("n");

        Confirm prompt = new Confirm("setTopic", "Do you want to specify topic?", true);

        prompt.prompt(command);

        verify(command.getTerminal().writer()).println(contains("Do you want to specify topic?"));

        when(lineReader.readLine(anyString())).thenReturn("No");
        prompt.prompt(command);

        when(lineReader.readLine(anyString())).thenReturn("nO");
        prompt.prompt(command);

        when(lineReader.readLine(anyString())).thenReturn("Y");
        prompt.prompt(command);

        verify(command, times(3)).setValue("setTopic", false);
        verify(command).setValue("setTopic", true);
    }
}
