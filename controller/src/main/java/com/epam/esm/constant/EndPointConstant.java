package com.epam.esm.constant;

/**
 * gift-certificates
 *
 * @author Dzmitry Platonov on 2019-10-23.
 * @version 0.0.1
 */
public class EndPointConstant {

    private EndPointConstant() {
    }

    public static final String CERTIFICATE_ENDPOINT = "certificates";
    public static final String DATA_GENERATOR_ENDPOINT = "generate";
    public static final String ORDER_ENDPOINT = "orders";
    public static final String REFRESH_TOKEN_ENDPOINT = "/authenticate/refresh-token";
    public static final String LOGIN_ENDPOINT = "/authenticate";
    public static final String TAG_ENDPOINT = "tags";
    public static final String USER_ENDPOINT = "users";
}
