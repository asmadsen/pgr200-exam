package no.kristiania.pgr200.commandline.interactive_commands;

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
        command.write(this.question + " [" + (this.defaultValue ? "Y/n" : "y/N") + "]");
        command.setValue(this.identifier, command.readNextLine());
    }
}
