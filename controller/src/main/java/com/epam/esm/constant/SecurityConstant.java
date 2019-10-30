package com.epam.esm.constant;

public class SecurityConstant {

    public static final String TOKEN_HEADER = "Authorization";
    public static final String ACCESS_TOKEN_EXPIRATION = "ExpiresIn";
    public static final String REFRESH_TOKEN_HEADER = "RefreshToken";
    public static final String TOKEN_PREFIX = "Bearer ";
    public static final String TOKEN_TYPE = "JWT";
    public static final String TOKEN_ISSUER = "secure-api";
    public static final String TOKEN_AUDIENCE = "secure-app";
    public static final String HEADER_CSRF_TOKEN = "X-XSRF-TOKEN";
    public static final String COOKIE_CSRF_TOKEN = "XSRF-TOKEN";
    public static final int JWT_TOKEN_DURATION_HOURS = 2;
    public static final int REFRESH_TOKEN_DURATION_DAYS = 60;

    private SecurityConstant() {
        throw new IllegalStateException("Cannot createJwt instance of static util class");
    }
}
