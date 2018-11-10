package no.kristiania.pgr200.common.annotations;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.UUID;

public class UUIDValidator implements ConstraintValidator<no.kristiania.pgr200.common.annotations.UUID, UUID> {

    protected String uuidPattern = "^[0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}$";
    private boolean allowNull;

    @Override
    public void initialize(no.kristiania.pgr200.common.annotations.UUID constraintAnnotation) {
        this.allowNull = constraintAnnotation.allowNull();
    }

    @Override
    public boolean isValid(UUID uuid, ConstraintValidatorContext uuidContext) {
        if (!allowNull) {
            if (uuid == null) return false;
        } else {
            return true;
        }
        return uuid.toString().matches(uuidPattern);
    }
}
