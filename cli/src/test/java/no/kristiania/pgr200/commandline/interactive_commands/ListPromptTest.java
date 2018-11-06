package no.kristiania.pgr200.commandline.interactive_commands;

import com.github.javafaker.Faker;
import org.jline.keymap.KeyMap;
import org.jline.reader.Binding;
import org.jline.reader.LineReader;
import org.jline.reader.impl.LineReaderImpl;
import org.jline.terminal.Terminal;
import org.jline.utils.InfoCmp;
import org.junit.Test;
import org.mockito.InOrder;

import java.io.PrintStream;
import java.io.PrintWriter;
import java.util.HashSet;
import java.util.Set;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

public class ListPromptTest {
    Faker faker = new Faker();
    @Test
    public void shouldListOptions() {
        Terminal stream = mock(Terminal.class);
        PrintWriter writer = mock(PrintWriter.class);
        LineReader lineReader = mock(LineReaderImpl.class);
        KeyMap<Binding> keys = spy(new KeyMap<>());
        when(lineReader.getTerminal()).thenReturn(stream);
        when(lineReader.getKeys()).thenReturn(keys);
        when(stream.writer()).thenReturn(writer);
        when(stream.getStringCapability(any())).thenReturn("up");

        InteractiveCommand command = spy(new InteractiveCommand(stream, lineReader));
        Set<String> characters = generateCharacters();
        String[] array = characters.toArray(new String[]{});
        ListPrompt listPrompt = new ListPrompt("name", "Who are you?", array);

        listPrompt.prompt(command);

        InOrder order = inOrder(writer);

        order.verify(writer).println(eq("Who are you?"));
        for (int i = 0; i < array.length; i++) {
            order.verify(writer).println(eq(String.format("  %s", array[i])));
        }

        verify(command).setValue("name", array[0]);
    }

    private Set<String> generateCharacters() {
        Set<String> characters = new HashSet<>();
        for (int i = 0; i < 10; i++) {
            characters.add(faker.rickAndMorty().character());
        }
        return characters;
    }
}