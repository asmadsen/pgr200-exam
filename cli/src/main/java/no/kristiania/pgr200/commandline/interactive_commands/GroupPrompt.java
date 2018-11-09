package no.kristiania.pgr200.commandline.interactive_commands;

public class GroupPrompt extends AbstractPrompt {
    private final AbstractPrompt[] prompts;

    public GroupPrompt(AbstractPrompt... prompts) {
        super(null);
        this.prompts = prompts;
    }

    @Override
    public void prompt(InteractiveCommand command) {
        for (AbstractPrompt prompt : this.prompts) {
            prompt.prompt(command);
        }
    }
}
