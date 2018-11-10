package no.kristiania.pgr200.commandline.interactive_commands;

import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

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

    public static PromptCreation input(String identifier, String question, String defaultValue) {
        return new PromptCreation().input(identifier, question, defaultValue);
    }

    public static PromptCreation input(String identifier, String question, String defaultValue, Class<?> validationClass) {
        return new PromptCreation().input(identifier, question, defaultValue, validationClass);
    }

    public static <T> PromptCreation list(String identifier, String question, T[] options) {
        return new PromptCreation().list(identifier, question, options);
    }

    public static <T> PromptCreation list(String identifier, String question, T[] options, Function<T, String> mapper) {
        return new PromptCreation().list(identifier, question, options, mapper);
    }

    public static <T> PromptCreation list(String identifier, String question, Supplier<T[]> options) {
        return new PromptCreation().list(identifier, question, options);
    }

    public static <T> PromptCreation list(String identifier, String question, Supplier<T[]> options, Function<T, String> mapper) {
        return new PromptCreation().list(identifier, question, options, mapper);
    }

    public static PromptCreation conditional(Predicate<InteractiveCommand> predicate, PromptCreation children) {
        return new PromptCreation().conditional(predicate, children);
    }
}
