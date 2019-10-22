package com.epam.esm.security;

/**
 * gift-certificates
 *
 * @author Dzmitry Platonov on 2019-10-22.
 * @version 0.0.1
 */
public class CsrfFilterException extends RuntimeException {

    public CsrfFilterException() {
    }

    public CsrfFilterException(String message) {
        super(message);
    }

    public CsrfFilterException(String message, Throwable cause) {
        super(message, cause);
    }

    public CsrfFilterException(Throwable cause) {
        super(cause);
    }
}
