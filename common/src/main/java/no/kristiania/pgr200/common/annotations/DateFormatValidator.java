package no.kristiania.pgr200.common.annotations;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class DateFormatValidator implements ConstraintValidator<no.kristiania.pgr200.common.annotations.DateFormat, String> {

    protected String datePattern = "^\\d{4}-\\d{2}-\\d{2}$";
    @Override
    public boolean isValid(String date, ConstraintValidatorContext uuidContext) {
        if ( date == null ) return false;
        return date.matches(datePattern);
    }
}
