package com.epam.esm.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class IllegalImageNameException extends RuntimeException {

    public IllegalImageNameException(String message) {
        super(message);
    }
}
