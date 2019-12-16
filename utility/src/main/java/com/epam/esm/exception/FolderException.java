package com.epam.esm.exception;

/**
 * utility
 *
 * @author Dzmitry Platonov on 2019-12-12.
 * @version 0.0.1
 */
public class FolderException extends RuntimeException {

    public FolderException() {
    }

    public FolderException(String message) {
        super(message);
    }

    public FolderException(String message, Throwable cause) {
        super(message, cause);
    }
}
