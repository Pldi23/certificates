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
 * @author Dzmitry Platonov on 2019-10-13.
 * @version 0.0.1
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = GiftCertificateSetValidator.class)
public @interface ValidCertificatesSet {

    String message() default "certificates input parameters should contain either id or name";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}
