package com.epam.esm.exception;

/**
 * gift-certificates
 *
 * @author Dzmitry Platonov on 2019-10-17.
 * @version 0.0.1
 */
public class InvalidUserException extends RuntimeException {

    public InvalidUserException() {
    }

    public InvalidUserException(String message) {
        super(message);
    }

    public InvalidUserException(String message, Throwable cause) {
        super(message, cause);
    }

    public InvalidUserException(Throwable cause) {
        super(cause);
    }
}
