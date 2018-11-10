package no.kristiania.pgr200.commandline.interactive_commands;

import org.fusesource.jansi.Ansi;
import org.jline.keymap.KeyMap;
import org.jline.reader.Binding;
import org.jline.reader.LineReader;
import org.jline.reader.Widget;
import org.jline.terminal.Terminal;
import org.jline.utils.InfoCmp;

import java.io.PrintWriter;
import java.util.Arrays;
import java.util.function.Function;
import java.util.function.Supplier;

import static org.fusesource.jansi.Ansi.ansi;
import static org.jline.reader.LineReader.MAIN;

public class ListPrompt<T> extends AbstractPrompt {
    private final String question;
    private T[] options;
    private final Supplier<T[]> supplier;
    private final Function<T, String> mapper;
    private int selectedIndex = 0;

    public ListPrompt(String identifier, String question, T[] options) {
        this(identifier, question, options, null);
    }

    public ListPrompt(String identifier, String question, T[] options, Function<T, String> mapper) {
        super(identifier);
        this.question = question;
        this.options = options;
        this.supplier = null;
        this.mapper = mapper;
        if (mapper == null && !(options instanceof String[])) {
            throw new RuntimeException("Mapper can only be null if T is of type String");
        }
    }

    public ListPrompt(String identifier, String question, Supplier<T[]> options) {
        this(identifier, question, options, null);
    }

    public ListPrompt(String identifier, String question, Supplier<T[]> options, Function<T, String> mapper) {
        super(identifier);
        this.question = question;
        this.supplier = options;
        this.options = null;
        this.mapper = mapper;
    }

    @Override
    public void prompt(InteractiveCommand command) {
        if (this.supplier != null && this.options == null) {
            this.options = this.supplier.get();
        }
        LineReader lineReader = command.getLineReader();
        PrintWriter output = command.getOutput();
        Terminal terminal = command.getTerminal();
        output.println(this.question);
        String[] options;
        if (this.mapper != null) {
            options = Arrays.stream(this.options).map(this.mapper).toArray(String[]::new);
        } else {
            options = (String[]) this.options;
        }

        this.selectedIndex = this.printList(terminal, options);

        this.registerKeyBindings(lineReader);
        terminal.puts(InfoCmp.Capability.cursor_invisible);
        lineReader.readLine();
        terminal.puts(InfoCmp.Capability.cursor_visible);

        this.unRegisterKeyBindings(lineReader);

        terminal.writer().print(ansi().cursorDown(this.options.length - this.selectedIndex + 1).a(""));
        terminal.writer().flush();

        command.setValue(this.identifier, this.options[selectedIndex]);
    }

    private void unRegisterKeyBindings(LineReader lineReader) {
        KeyMap<Binding> keys = lineReader.getKeys();
        Terminal terminal = lineReader.getTerminal();
        keys.unbind(KeyMap.key(terminal, InfoCmp.Capability.key_up));
        keys.unbind(KeyMap.key(terminal, InfoCmp.Capability.key_down));
    }

    private void registerKeyBindings(LineReader lineReader) {
        KeyMap<Binding> keys = lineReader.getKeyMaps().get(MAIN);
        Terminal terminal = lineReader.getTerminal();
        keys.bind((Widget) () -> {
            int previousIndex = this.selectedIndex;
            this.replaceLine(terminal, this.map(this.options[this.selectedIndex--]), false);
            if (this.selectedIndex == -1) this.selectedIndex = this.options.length - 1;
            this.goToLine(terminal, previousIndex, this.selectedIndex);
            this.replaceLine(terminal, this.map(this.options[this.selectedIndex]), true);
            return false;
        }, KeyMap.key(terminal, InfoCmp.Capability.key_up));


        keys.bind((Widget) () -> {
            int previousIndex = this.selectedIndex;
            this.replaceLine(terminal, this.map(this.options[this.selectedIndex++]), false);
            if (this.selectedIndex == this.options.length) this.selectedIndex = 0;
            this.goToLine(terminal, previousIndex, this.selectedIndex);
            this.replaceLine(terminal, this.map(this.options[this.selectedIndex]), true);
            return false;
        }, KeyMap.key(terminal, InfoCmp.Capability.key_down));
    }

    private String map(T option) {
        if (this.mapper != null) {
            return this.mapper.apply(option);
        }
        return (String) option;
    }

    private void goToLine(Terminal terminal, int from, int to) {
        while (from != to) {
            if (from < to) {
                terminal.puts(InfoCmp.Capability.cursor_down);
                from++;
            } else {
                terminal.puts(InfoCmp.Capability.cursor_up);
                from--;
            }
        }
    }

    private int printList(Terminal terminal, String[] list) {
        for (String entry : list) {
            terminal.writer().println(String.format("  %s", entry));
        }
        int index = list.length + 1;
        terminal.writer().println(ansi().fgBrightBlack().a("(use arrow keys)").reset());
        this.goToLine(terminal, index, 0);
        index = 0;
        this.replaceLine(terminal, list[index], true);
        return index;
    }

    private void replaceLine(Terminal terminal, String line, boolean selected) {
        terminal.puts(InfoCmp.Capability.carriage_return);
        terminal.puts(InfoCmp.Capability.clr_eol);
        String prefix = selected ? "> " : "  ";
        terminal.writer()
                .print(ansi().fgBright(selected ? Ansi.Color.CYAN : Ansi.Color.DEFAULT)
                             .a(String.format("%s%s", prefix, line))
                             .reset());
    }
}
