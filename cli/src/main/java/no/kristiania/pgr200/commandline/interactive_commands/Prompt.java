package no.kristiania.pgr200.commandline.interactive_commands;

import java.util.function.Predicate;

public class Prompt {
    public static PromptCreation confirm(String identifier, String question, boolean defaultValue) {
        return new PromptCreation().confirm(identifier, question, defaultValue);
    }

    public static PromptCreation input(String identifier, String question) {
        return new PromptCreation().input(identifier, question);
    }

    public static PromptCreation input(String identifier, String question, Class<?> validationClass) {
        return new PromptCreation().input(identifier, question, validationClass);
    }

    public static PromptCreation list(String identifier, String question, String[] options) {
        return new PromptCreation().list(identifier, question, options);
    }

    public static PromptCreation conditional(Predicate<InteractiveCommand> predicate, PromptCreation children) {
        return new PromptCreation().conditional(predicate, children);
    }
}
