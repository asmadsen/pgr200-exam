package no.kristiania.pgr200.commandline.interactive_commands;

import com.github.javafaker.Faker;
import no.kristiania.pgr200.common.models.Talk;
import org.jline.keymap.KeyMap;
import org.jline.reader.Binding;
import org.jline.reader.LineReader;
import org.jline.terminal.Terminal;

import java.io.PrintWriter;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import static org.mockito.Mockito.*;

public class Mocks {
    private static Faker faker = new Faker();

    public static InteractiveCommand getInteractiveCommand() {
        LineReader lineReader = getLineReader();

        return spy(new InteractiveCommand(lineReader));
    }

    public static LineReader getLineReader() {
        LineReader lineReader = mock(LineReader.class);
        Terminal terminal = getTerminal();
        KeyMap<Binding> keys = new KeyMap<>();
        Map<String, KeyMap<Binding>> keyMaps = mock(Map.class);
        when(keyMaps.get(anyString())).thenReturn(keys);
        when(lineReader.getTerminal()).thenReturn(terminal);
        when(lineReader.getKeys()).thenReturn(keys);
        when(lineReader.getKeyMaps()).thenReturn(keyMaps);
        return lineReader;
    }

    public static PrintWriter getPrintWriter() {
        return mock(PrintWriter.class);
    }

    public static Terminal getTerminal() {
        Terminal terminal = mock(Terminal.class);

        when(terminal.writer()).thenReturn(getPrintWriter());

        return terminal;
    }

    public static String[] generateCharacters() {
        Set<String> characters = new HashSet<>();
        for (int i = 0; i < 10; i++) {
            characters.add(Mocks.faker.rickAndMorty().character());
        }
        return characters.toArray(new String[]{});
    }

    public static Talk[] generateTalks() {
        Set<Talk> characters = new HashSet<>();
        for (int i = 0; i < 10; i++) {
            Talk talk = new Talk();
            talk.title = faker.book().title() + " - " + i;
            talk.description = faker.harryPotter().quote();
            characters.add(talk);
        }
        return characters.toArray(new Talk[]{});
    }
}
