package no.kristiania.pgr200.common.utils;

import no.kristiania.pgr200.common.models.Talk;

import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.function.Function;
import java.util.function.Supplier;

public class Utils {
    private static Validator validator;

    public static Validator validator() {
        if (Utils.validator == null) {
            ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
            Utils.validator = factory.getValidator();
        }
        return Utils.validator;
    }

    public static Object primitiveToClassType(Class<?> input) {
        switch (input.getSimpleName()) {
            case "int":
                return Integer.class;
            default:
                return input;
        }
    }
}
