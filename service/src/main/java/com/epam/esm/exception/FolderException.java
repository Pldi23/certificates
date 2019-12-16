package com.epam.esm.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.EXPECTATION_FAILED)
public class FolderException extends RuntimeException {

    public FolderException(String message) {
        super(message);
    }

    public FolderException(String message, Throwable cause) {
        super(message, cause);
    }
}
