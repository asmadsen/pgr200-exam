package no.kristiania.pgr200.commandline.interactive_commands;

import no.kristiania.pgr200.common.utils.Utils;
import org.hibernate.validator.internal.constraintvalidators.bv.NotNullValidator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import java.util.Set;

import static org.fusesource.jansi.Ansi.ansi;

public class Input<T> extends AbstractPrompt {
    private final String question;
    private final String defaultValue;
    private final Class<T> validatorClass;

    public Input(String identifier, String question) {
        this(identifier, question, (String) null);
    }

    public Input(String identifier, String question, Class<T> validatorClass) {
        this(identifier, question, null, validatorClass);
    }

    public Input(String identifier, String question, String defaultValue) {
        super(identifier);
        this.question = question;
        this.defaultValue = defaultValue;
        this.validatorClass = null;
    }

    public Input(String identifier, String question, String defaultValue, Class<T> validatorClass) {
        super(identifier);
        this.question = question;
        this.defaultValue = defaultValue;
        this.validatorClass = validatorClass;
    }

    public void prompt(InteractiveCommand command) {
        String input;
        String questionLine = this.question + " ";
        if (this.defaultValue != null) {
            questionLine += ansi().fgBrightBlack().a(this.defaultValue).reset();
        }
        do {
            command.getOutput().println(questionLine);
            input = command.getLineReader().readLine("> ");
            if (input.isEmpty() && this.defaultValue != null) {
                input = this.defaultValue;
            }
        } while (!this.validateInput(input));
        command.setValue(this.identifier, input);
    }

    private boolean validateInput(String input) {
        if (this.validatorClass == null) return true;
        Set<ConstraintViolation<T>> set = Utils.validator().validateValue(this.validatorClass, this.identifier, input);
        return set.isEmpty();
    }
}
