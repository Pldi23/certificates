package com.epam.esm.exception;

/**
 * gift-certificates
 *
 * @author Dzmitry Platonov on 2019-10-21.
 * @version 0.0.1
 */
public class GenerateDataException extends RuntimeException {

    public GenerateDataException() {
    }

    public GenerateDataException(String message) {
        super(message);
    }

    public GenerateDataException(String message, Throwable cause) {
        super(message, cause);
    }

    public GenerateDataException(Throwable cause) {
        super(cause);
    }
}
