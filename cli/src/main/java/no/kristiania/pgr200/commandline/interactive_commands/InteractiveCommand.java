package no.kristiania.pgr200.commandline.interactive_commands;

import org.jline.reader.LineReader;
import org.jline.reader.LineReaderBuilder;
import org.jline.terminal.Terminal;
import org.jline.terminal.TerminalBuilder;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

public class InteractiveCommand {
    private Map<String, Object> values = new HashMap<>();

    private final Terminal terminal;
    private final LineReader lineReader;

    public InteractiveCommand(Terminal terminal, LineReader lineReader) {
        this.terminal = terminal;
        this.lineReader = lineReader;
    }

    public InteractiveCommand(Terminal terminal) {
        this.terminal = terminal;
        this.lineReader = LineReaderBuilder.builder()
                .terminal(terminal)
                .build();
        this.lineReader.variable("disable-history", true);
    }

    public InteractiveCommand() throws IOException {
        this(TerminalBuilder.builder()
                            .jansi(true)
                            .build());
    }


    public void setValue(String key, Object value) {
        this.values.put(key, value);
    }

    public Map<String, Object> getValues() {
        return this.values;
    }

    public String readNextLine() {
        return null;
    }

    public void write(String message) {

    }

    public LineReader getLineReader() {
        return this.lineReader;
    }

    public Terminal getTerminal() {
        return terminal;
    }

    public PrintWriter getOutput() {
        return this.terminal.writer();
    }
}
