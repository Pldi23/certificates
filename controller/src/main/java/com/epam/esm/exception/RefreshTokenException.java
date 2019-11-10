package com.epam.esm.exception;

/**
 * gift-certificates
 *
 * @author Dzmitry Platonov on 2019-11-09.
 * @version 0.0.1
 */
public class RefreshTokenException extends RuntimeException {

    public RefreshTokenException() {
    }

    public RefreshTokenException(String message) {
        super(message);
    }

    public RefreshTokenException(String message, Throwable cause) {
        super(message, cause);
    }

    public RefreshTokenException(Throwable cause) {
        super(cause);
    }
}
