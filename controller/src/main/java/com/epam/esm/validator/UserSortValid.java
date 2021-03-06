package com.epam.esm.validator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = UserSortValidator.class)
public @interface UserSortValid {

    String message() default "Please provide a valid sort parameter (id/role/num_orders)";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
