package com.epam.esm.security;

/**
 * gift-certificates
 *
 * @author Dzmitry Platonov on 2019-10-16.
 * @version 0.0.1
 */
public class SecurityConstants {

    public static final String AUTH_LOGIN_URL = "/authenticate";
    public static final String AUTH_REFRESH_TOKEN_URL = "/authenticate/refresh-token";

    public static final String JWT_SECRET = "n2r5u8x/A%D*G-KaPdSgVkYp3s6v9y$B&E(H+MbQeThWmZq4t7w!z%C*F-J@NcRf";

    public static final String TOKEN_HEADER = "Authorization";
    public static final String ACCESS_TOKEN_EXPIRATION = "ExpiresIn";
    public static final String REFRESH_TOKEN_HEADER = "RefreshToken";
    public static final String TOKEN_PREFIX = "Bearer ";
    public static final String TOKEN_TYPE = "JWT";
    public static final String TOKEN_ISSUER = "secure-api";
    public static final String TOKEN_AUDIENCE = "secure-app";
    public static final int JWT_TOKEN_DURATION_HOURS = 2;
    public static final int REFRESH_TOKEN_DURATION_DAYS = 60;

    private SecurityConstants() {
        throw new IllegalStateException("Cannot createJwt instance of static util class");
    }
}
