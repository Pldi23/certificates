package com.epam.esm.validator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = PageAndSizeValidator.class)
public @interface PageAndSizeValid {
    String message() default
            "Please provide a valid request... Ex: ?page=1&size=2 . (If size parameter not set - page parameter must be null or 1)";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
