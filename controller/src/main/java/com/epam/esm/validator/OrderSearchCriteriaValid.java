package com.epam.esm.validator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = OrderSearchCriteriaValidator.class)
public @interface OrderSearchCriteriaValid {
    String message() default "Please provide a valid request... Ex: ?email=pldi@mail.ru&c_id=1&page=1&size=2. " +
            "search by user email/id (user_id/email) and by certificate name/id acceptable (c_name/c_id)";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
