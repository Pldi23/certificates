package com.epam.esm.exception;

/**
 * gift-certificates
 *
 * @author Dzmitry Platonov on 2019-10-13.
 * @version 0.0.1
 */
public class UserRoleException extends RuntimeException {
    public UserRoleException() {
    }

    public UserRoleException(String message) {
        super(message);
    }

    public UserRoleException(String message, Throwable cause) {
        super(message, cause);
    }

    public UserRoleException(Throwable cause) {
        super(cause);
    }
}
