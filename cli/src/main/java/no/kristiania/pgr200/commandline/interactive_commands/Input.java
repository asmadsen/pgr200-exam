package no.kristiania.pgr200.commandline.interactive_commands;

import no.kristiania.pgr200.common.utils.Utils;
import org.hibernate.validator.internal.constraintvalidators.bv.NotNullValidator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import java.util.Set;

public class Input<T> extends AbstractPrompt {
    private final String question;
    private final Class<T> validatorClass;

    public Input(String identifier, String question) {
        super(identifier);
        this.question = question;
        this.validatorClass = null;
    }

    public Input(String identifier, String question, Class<T> validatorClass) {
        super(identifier);
        this.question = question;
        this.validatorClass = validatorClass;
    }

    public void prompt(InteractiveCommand command) {
        String input;
        do {
            command.getOutput().println(this.question + " ");
            input = command.getLineReader().readLine("> ");
        } while (!this.validateInput(input));
        command.setValue(this.identifier, input);
    }

    private boolean validateInput(String input) {
        if (this.validatorClass == null) return true;
        Set<ConstraintViolation<T>> set = Utils.validator().validateValue(this.validatorClass, this.identifier, input);
        return set.isEmpty();
    }
}
