package no.kristiania.pgr200.commandline.interactive_commands;

import static org.fusesource.jansi.Ansi.ansi;

public class Confirm extends AbstractPrompt {
    private final String question;
    private final boolean defaultValue;

    public Confirm(String identifier, String question, boolean defaultValue) {
        super(identifier);
        this.question = question;
        this.defaultValue = defaultValue;
    }

    @Override
    public void prompt(InteractiveCommand command) {
        command.getOutput().println(String.format(
                "%s %s",
                this.question,
                ansi().fgBrightBlack().a("(" + (this.defaultValue ? "Y/n" : "y/N") + ")").reset()));

        boolean value = this.defaultValue;;
        String input = command.getLineReader().readLine("> ");

        if (input.matches("(?i)y(es)?")) {
            value = true;
        } else if (input.matches("(?i)n(o)?")) {
            value = false;
        }

        command.setValue(this.identifier, value);
    }
}
