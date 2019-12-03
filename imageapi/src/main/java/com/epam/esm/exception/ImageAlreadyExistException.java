package com.epam.esm.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class ImageAlreadyExistException extends RuntimeException {

    public ImageAlreadyExistException(String message, Throwable cause) {
        super(message, cause);
    }
}
