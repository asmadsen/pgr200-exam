package no.kristiania.pgr200.commandline;

import no.kristiania.pgr200.commandline.handlers.TalksHandler;
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
import java.util.Map;

import static no.kristiania.pgr200.commandline.interactive_commands.Prompt.confirm;
import static no.kristiania.pgr200.commandline.interactive_commands.Prompt.list;

public class ConferenceCliClient {
    private static Logger logger = LoggerFactory.getLogger(ConferenceCliClient.class);
    private static boolean restart = true;

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

            while (restart) {
                InteractiveCommand command = new InteractiveCommand(lineReader);

                String[] models = {"Talks"};

                command.pushPrompt(
                        list("model", "Choose startingpoint", models)
                                .getPromptsAsPrompt()
                );

                command.prompt();

                Map<String, Object> values = command.getValues();

                switch (((String) values.get("model")).toLowerCase()) {
                    case "talks":
                        new TalksHandler(lineReader).execute();
                        break;
                }

                command = new InteractiveCommand(lineReader);
                command.pushPrompt(confirm("restart", "Do you want to do another action", false).getPromptsAsPrompt());
                command.prompt();
                if (!(Boolean) command.getValues().get("restart")) {
                    restart = false;
                }
            }


        } catch (Exception e) {
            logger.error("Main", e);
        } finally {
            if (terminal != null) {
                terminal.puts(InfoCmp.Capability.cursor_visible);
            }
        }

    }
}
