package no.kristiania.pgr200.commandline.interactive_commands;

import no.kristiania.pgr200.common.models.Talk;
import org.jline.keymap.KeyMap;
import org.jline.reader.Binding;
import org.jline.reader.LineReader;
import org.jline.reader.Widget;
import org.jline.terminal.Terminal;
import org.junit.Test;
import org.mockito.InOrder;
import org.mockito.stubbing.Answer;

import java.io.PrintWriter;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

public class ListPromptTest {
    @Test
    public void shouldListOptions() {
        InteractiveCommand command = Mocks.getInteractiveCommand();

        Terminal terminal = command.getTerminal();
        PrintWriter writer = command.getOutput();

        when(terminal.getStringCapability(any()))
                .thenReturn("up")
                .thenReturn("down")
                .thenReturn("up")
                .thenReturn("down");

        LineReader lineReader = command.getLineReader();

        when(lineReader.readLine()).then((Answer<String>) (invocation) -> {
            KeyMap<Binding> keys = lineReader.getKeys();
            ((Widget) keys.getBound("down")).apply();
            ((Widget) keys.getBound("up")).apply();
            ((Widget) keys.getBound("down")).apply();
            return "";
        });

        String[] characters = Mocks.generateCharacters();
        ListPrompt listPrompt = new ListPrompt("name", "Who are you?", characters);

        listPrompt.prompt(command);

        InOrder order = inOrder(writer);

        order.verify(writer).println(eq("Who are you?"));
        for (String anArray : characters) {
            order.verify(writer).println(eq(String.format("  %s", anArray)));
        }

        verify(command).setValue("name", characters[1]);
    }

    @Test
    public void shouldBeAbleToMapOptions() {
        InteractiveCommand command = Mocks.getInteractiveCommand();

        Terminal terminal = command.getTerminal();
        PrintWriter writer = command.getOutput();

        when(terminal.getStringCapability(any()))
                .thenReturn("up")
                .thenReturn("down")
                .thenReturn("up")
                .thenReturn("down");

        LineReader lineReader = command.getLineReader();

        when(lineReader.readLine()).then((Answer<String>) (invocation) -> {
            KeyMap<Binding> keys = lineReader.getKeys();
            ((Widget) keys.getBound("down")).apply();
            ((Widget) keys.getBound("up")).apply();
            ((Widget) keys.getBound("down")).apply();
            return "";
        });

        Talk[] talks = Mocks.generateTalks();
        ListPrompt listPrompt = new ListPrompt<>("name", "Who are you?", talks, Talk::getTitle);

        listPrompt.prompt(command);

        InOrder order = inOrder(writer);

        order.verify(writer).println(eq("Who are you?"));
        for (Talk talk : talks) {
            order.verify(writer, atLeast(1)).println(eq(String.format("  %s", talk.title)));
        }

        verify(command).setValue("name", talks[1]);
    }

}