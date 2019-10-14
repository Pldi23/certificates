package com.epam.esm.exception;

/**
 * gift-certificates
 *
 * @author Dzmitry Platonov on 2019-10-11.
 * @version 0.0.1
 */
public class EntityAlreadyExistsException extends RuntimeException {

    public EntityAlreadyExistsException() {
    }

    public EntityAlreadyExistsException(String message) {
        super(message);
    }

    public EntityAlreadyExistsException(String message, Throwable cause) {
        super(message, cause);
    }

    public EntityAlreadyExistsException(Throwable cause) {
        super(cause);
    }
}
