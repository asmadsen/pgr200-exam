package no.kristiania.pgr200.common.annotations;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.UUID;

public class UUIDValidator implements ConstraintValidator<no.kristiania.pgr200.common.annotations.UUID, UUID> {

    protected String uuidPattern = "^[0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}$";
    @Override
    public boolean isValid(UUID uuid, ConstraintValidatorContext uuidContext) {
        if ( uuid == null ) return false;
        return uuid.toString().matches(uuidPattern);
    }
}
