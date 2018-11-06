package no.kristiania.pgr200.commandline.interactive_commands;

public abstract class AbstractPrompt {
    protected final String identifier;

    public AbstractPrompt(String identifier) {
        this.identifier = identifier;
    }

    public abstract void prompt(InteractiveCommand command);
}
