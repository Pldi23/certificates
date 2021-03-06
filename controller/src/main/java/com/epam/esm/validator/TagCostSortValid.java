package com.epam.esm.validator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = TagCostSortValidator.class)
public @interface TagCostSortValid {
    String message() default "Please provide a valid cost-sort parameter";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
