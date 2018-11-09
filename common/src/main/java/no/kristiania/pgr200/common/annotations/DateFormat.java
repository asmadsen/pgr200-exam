package no.kristiania.pgr200.common.annotations;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Constraint(validatedBy={ DateFormatValidator.class })
@Retention(RetentionPolicy.RUNTIME)
public @interface DateFormat {
    String message() default "date format must be YYYY-MM-DD";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}
