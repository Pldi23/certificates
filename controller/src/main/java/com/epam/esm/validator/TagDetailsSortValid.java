package com.epam.esm.validator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * gift-certificates
 *
 * @author Dzmitry Platonov on 2019-10-31.
 * @version 0.0.1
 */
@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = TagDetailsSortValidator.class)
public @interface TagDetailsSortValid {

    String message() default "Please provide a valid sort parameter";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
