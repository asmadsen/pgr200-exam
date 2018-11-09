package no.kristiania.pgr200.commandline.interactive_commands;

import org.jline.reader.LineReader;
import org.jline.terminal.Terminal;

import java.io.PrintWriter;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class InteractiveCommand {
    private final Terminal terminal;
    private final LineReader lineReader;
    private Map<String, Object> values = new HashMap<>();
    private List<AbstractPrompt> prompts = new LinkedList<>();

    public InteractiveCommand(LineReader lineReader) {
        this.terminal = lineReader.getTerminal();
        this.lineReader = lineReader;
    }

    public void setValue(String key, Object value) {
        this.values.put(key, value);
    }

    public Map<String, Object> getValues() {
        return this.values;
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

    public void pushPrompt(AbstractPrompt prompt) {
        this.prompts.add(prompt);
    }

    public void prompt() {
        for (AbstractPrompt prompt : this.prompts) {
            prompt.prompt(this);
        }
    }
}
