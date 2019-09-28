package com.epam.esm.exception;

/**
 * giftcertificates
 *
 * @author Dzmitry Platonov on 2019-09-27.
 * @version 0.0.1
 */
public class ApplicationDataSourceException extends RuntimeException {

    public ApplicationDataSourceException(String message, Throwable cause) {
        super(message, cause);
    }

}
