package com.epam.esm.validate;

public class ValidationException extends RuntimeException {

    ValidationException(String message) {
        super(message);
    }
}
