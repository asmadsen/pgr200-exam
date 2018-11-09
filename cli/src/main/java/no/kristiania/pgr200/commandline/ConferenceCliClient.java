package no.kristiania.pgr200.commandline;

import no.kristiania.pgr200.commandline.interactive_commands.InteractiveCommand;
import org.jline.reader.LineReader;
import org.jline.reader.LineReaderBuilder;
import org.jline.terminal.Terminal;
import org.jline.terminal.TerminalBuilder;
import org.jline.utils.InfoCmp;
import org.logevents.LogEventFactory;
import org.logevents.observers.NullLogEventObserver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.event.Level;

import java.io.IOException;

import static no.kristiania.pgr200.commandline.interactive_commands.Prompt.list;

public class ConferenceCliClient {
    private static Logger logger = LoggerFactory.getLogger(ConferenceCliClient.class);

    public ConferenceCliClient() {
    }

    public static void main(String[] args) throws IOException {
        LogEventFactory logEventFactory = LogEventFactory.getInstance();

        logEventFactory.setRootLevel(Level.ERROR);
        logEventFactory.setRootObserver(new NullLogEventObserver());

        Terminal terminal = null;
        try {
            terminal = TerminalBuilder.builder()
                                      .jansi(true)
                                      .signalHandler(Terminal.SignalHandler.SIG_IGN)
                                      .nativeSignals(true)
                                      .build();
            terminal.enterRawMode();
            LineReader lineReader = LineReaderBuilder.builder()
                                                     .terminal(terminal)
                                                     .build();
            lineReader.variable("disable-history", true);

            InteractiveCommand command = new InteractiveCommand(lineReader);

            String[] models = {"Talks", "Tracks"};
            String[] actions = {"List", "Add", "Delete"};

            command.pushPrompt(
                    list("model", "Choose startingpoint", models)
                            .list("action", "What action do you want to take?", actions)
                            .getPromptsAsPrompt()
            );

            command.prompt();

        } catch (Exception e) {
            logger.error("Main", e);
        } finally {
            if (terminal != null) {
                terminal.puts(InfoCmp.Capability.cursor_visible);
            }
        }

    }
}
