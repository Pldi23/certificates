package com.epam.esm.validator;

import com.epam.esm.exception.DateNotValidException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class ExpirationDateValidator {

    private ResourceBundleMessageSource messageSource;

    @Autowired
    public ExpirationDateValidator(ResourceBundleMessageSource messageSource) {
        this.messageSource = messageSource;
    }

    public void isValidDate(LocalDate creationDate, LocalDate expirationDate) {
        if (creationDate != null && expirationDate != null && creationDate.isAfter(expirationDate)) {
            throw new DateNotValidException(messageSource.getMessage("violation.date", null, null));
        }
    }

}
