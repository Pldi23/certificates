package com.epam.esm.validator;

import com.epam.esm.exception.DateNotValidException;
import com.epam.esm.util.Translator;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class ExpirationDateValidator {

    public void isValidDate(LocalDate creationDate, LocalDate expirationDate) {
        if (creationDate != null && expirationDate != null && creationDate.isAfter(expirationDate)) {
            throw new DateNotValidException(Translator.toLocale("violation.creation.date"));
        }
    }

}
