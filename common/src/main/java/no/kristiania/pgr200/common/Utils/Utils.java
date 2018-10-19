package no.kristiania.pgr200.common.Utils;

import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

public class Utils {
    private static Validator validator;

    public static Validator validator() {
        if (Utils.validator == null) {
            ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
            Utils.validator = factory.getValidator();
        }
        return Utils.validator;
    }
}
