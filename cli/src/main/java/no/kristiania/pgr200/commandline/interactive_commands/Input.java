package no.kristiania.pgr200.commandline.interactive_commands;

public class Input extends AbstractPrompt {
    private final String question;

    public Input(String identifier, String question) {
        super(identifier);
        this.question = question;
    }

    public void prompt(InteractiveCommand command) {
        command.write(this.question + " ");
        command.setValue(this.identifier, command.readNextLine());
    }
}
