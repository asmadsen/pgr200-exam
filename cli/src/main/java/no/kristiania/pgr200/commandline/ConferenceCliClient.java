package no.kristiania.pgr200.commandline;

import com.github.javafaker.Faker;
import no.kristiania.pgr200.commandline.commands.ConferenceClientCommand;
import no.kristiania.pgr200.commandline.exceptions.DecodeCommandException;
import no.kristiania.pgr200.commandline.interactive_commands.InteractiveCommand;
import no.kristiania.pgr200.commandline.interactive_commands.ListPrompt;
import org.jline.keymap.KeyMap;
import org.jline.reader.Binding;
import org.jline.reader.LineReader;
import org.jline.reader.LineReaderBuilder;
import org.jline.reader.Widget;
import org.jline.terminal.Terminal;
import org.jline.terminal.TerminalBuilder;
import org.jline.utils.InfoCmp;
import org.jline.utils.NonBlockingReader;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;

public class ConferenceCliClient {
    static int currentIndex = 0;
    private Map<String, Class<? extends ConferenceClientCommand>> commands = new HashMap<>();

    public ConferenceCliClient() {
    }

    public static void main(String[] args) throws IOException {
        Terminal terminal = null;
        LineReader lineReader = null;
        NonBlockingReader reader = null;
        try {
            TerminalBuilder builder = TerminalBuilder.builder()
                                                     .jansi(true)
                                                     .signalHandler(Terminal.SignalHandler.SIG_IGN)
                                                     .nativeSignals(true);
            terminal = builder.build();
            terminal.enterRawMode();
            reader = terminal.reader();
            lineReader = LineReaderBuilder.builder()
                                          .terminal(terminal)
                                          .build();
            lineReader.variable("disable-history", true);

            lineReader.readLine();
            terminal.puts(InfoCmp.Capability.clear_screen);

            InteractiveCommand command = new InteractiveCommand(terminal, lineReader);

            Faker faker = new Faker();
            Set<String> characters = new HashSet<>();
            for (int i = 0; i < 10; i++) {
                characters.add(faker.rickAndMorty().character());
            }
            String[] values = characters.toArray(new String[]{});

            ListPrompt prompt = new ListPrompt("name", "Who are you?", values);

            prompt.prompt(command);

            String character = (String) command.getValues().get("name");
            terminal.puts(InfoCmp.Capability.clear_screen);
            terminal.writer().println("The character was: " + character);

            String line;
            while (!Thread.interrupted()) {
                line = lineReader.readLine();

                if (line == null) {
                    continue;
                }
                if (line.equalsIgnoreCase("show")) {
                    terminal.puts(InfoCmp.Capability.cursor_visible);
                }

                if (line.equalsIgnoreCase("quit")) {
                    terminal.puts(InfoCmp.Capability.cursor_visible);
                    terminal.flush();
                    //thread.interrupt();
                    System.exit(0);
                    break;
                }
            }


        } catch (Exception ignored) {
            ignored.printStackTrace();
        } finally {
            if (reader != null) {
                reader.close();
            }
            if (terminal != null) {
                terminal.puts(InfoCmp.Capability.cursor_visible);
                terminal.flush();
                terminal.close();
            }
        }
    }

    private static String select(LineReader lineReader) {
        Terminal terminal = lineReader.getTerminal();
        Faker faker = new Faker();
        Set<String> characters = new HashSet<>();
        for (int i = 0; i < 10; i++) {
            characters.add(faker.rickAndMorty().character());
        }
        String[] values = characters.toArray(new String[]{});

        PrintWriter writer = terminal.writer();
        for (String value : values) {
            writer.println("  " + value);
            currentIndex++;
        }
        while (currentIndex > 0) {
            terminal.puts(InfoCmp.Capability.cursor_up);
            currentIndex--;
        }
        clearAndRedraw(terminal, values[currentIndex], true);


        int maxLength = values.length;

        KeyMap<Binding> keys = lineReader.getKeys();
        keys.bind((Widget) () -> {
            if (currentIndex > 0) {
                clearAndRedraw(terminal, values[currentIndex--], false);
                terminal.puts(InfoCmp.Capability.cursor_up);
                clearAndRedraw(terminal, values[currentIndex], true);
            }
            return false;
        }, KeyMap.key(terminal, InfoCmp.Capability.key_up));


        keys.bind((Widget) () -> {
            if (currentIndex < maxLength) {
                clearAndRedraw(terminal, values[currentIndex++], false);
                terminal.puts(InfoCmp.Capability.cursor_down);
                clearAndRedraw(terminal, values[currentIndex], true);
            }
            return false;
        }, KeyMap.key(terminal, InfoCmp.Capability.key_down));

        terminal.puts(InfoCmp.Capability.cursor_invisible);

        lineReader.readLine();

        terminal.puts(InfoCmp.Capability.cursor_visible);

        return values[currentIndex];
    }

    private static void clearAndRedraw(Terminal terminal, String value, boolean selected) {
        terminal.puts(InfoCmp.Capability.carriage_return);
        terminal.puts(InfoCmp.Capability.clr_eol);
        if (selected) {
            terminal.writer().print("❯ " + value);
        } else {
            terminal.writer().print("  " + value);
        }
    }


    public ConferenceClientCommand decodeCommand(String[] strings) {
        String commandString = strings[0].toLowerCase();
        ConferenceClientCommand command;
        if (this.commands.containsKey(commandString)) {
            try {
                command = this.commands.get(commandString).newInstance();
            } catch (InstantiationException | IllegalAccessException e) {
                throw new DecodeCommandException();
            }
        } else {
            throw new DecodeCommandException();
        }
        CommandOptions options = new CommandOptions(Arrays.copyOfRange(strings, 1, strings.length));

        return command.execute(options);
    }

    public void register(String commandName, Class<? extends ConferenceClientCommand> command) {
        this.commands.put(commandName, command);
    }

    enum Action {
        Up, Left, Right, Down,
        Quit, Retry
    }
}
