package no.kristiania.pgr200.commandline.interactive_commands;

import java.util.function.Predicate;

public class ConditionalPrompt extends AbstractPrompt {
    private final Predicate<InteractiveCommand> predicate;
    private final AbstractPrompt childPrompt;

    public ConditionalPrompt(Predicate<InteractiveCommand> predicate, AbstractPrompt childPrompt) {
        super(null);
        this.predicate = predicate;
        this.childPrompt = childPrompt;
    }

    @Override
    public void prompt(InteractiveCommand command) {
        if (predicate.test(command)) {
            childPrompt.prompt(command);
        }
    }
}
