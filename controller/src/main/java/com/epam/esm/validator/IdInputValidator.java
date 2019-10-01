package com.epam.esm.validator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

/**
 * giftcertificates
 *
 * @author Dzmitry Platonov on 2019-09-30.
 * @version 0.0.1
 */
@Component
public class IdInputValidator implements Validator {

    private ResourceBundleMessageSource messageSource;

    @Autowired
    public IdInputValidator(ResourceBundleMessageSource messageSource) {
        this.messageSource = messageSource;
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return Long.class.isAssignableFrom(aClass);
    }

    @Override
    public void validate(Object o, Errors errors) {
        long id = (long) o;
        if (id < 0) {
            errors.reject(messageSource.getMessage( "violation.id", null, null));
        }
    }
}
