package no.kristiania.pgr200.commandline.interactive_commands;

import java.util.LinkedList;
import java.util.function.Predicate;

public class PromptCreation {
    private LinkedList<AbstractPrompt> prompts;

    protected PromptCreation() {
        this.prompts = new LinkedList<>();
    }

    public PromptCreation confirm(String identifier, String question, boolean defaultValue) {
        this.addPrompt(new Confirm(identifier, question, defaultValue));
        return this;
    }

    public PromptCreation input(String identifier, String question) {
        this.addPrompt(new Input(identifier, question));
        return this;
    }

    public PromptCreation input(String identifier, String question, Class<?> validationClass) {
        this.addPrompt(new Input<>(identifier, question, validationClass));
        return this;
    }

    public PromptCreation list(String identifier, String question, String[] options) {
        this.addPrompt(new ListPrompt(identifier, question, options));
        return this;
    }

    public PromptCreation conditional(Predicate<InteractiveCommand> predicate, PromptCreation children) {
        this.addPrompt(new ConditionalPrompt(predicate, children.getPromptsAsPrompt()));
        return this;
    }

    public PromptCreation conditional(Predicate<InteractiveCommand> predicate, AbstractPrompt child) {
        this.addPrompt(new ConditionalPrompt(predicate, child));
        return this;
    }

    public AbstractPrompt getPromptsAsPrompt() {
        if (this.prompts.size() == 1) {
            return this.prompts.get(0);
        }
        return new GroupPrompt(this.prompts.toArray(new AbstractPrompt[]{}));
    }

    public void addPrompt(AbstractPrompt prompt) {
        this.prompts.add(prompt);
    }
}
