package com.epam.esm.security;

import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * gift-certificates
 *
 * @author Dzmitry Platonov on 2019-10-18.
 * @version 0.0.1
 */
@Log4j2
public class FailedHandler implements AuthenticationFailureHandler {
    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
        log.warn("unsuccessful authentication because of " + exception.getMessage());
        response.setStatus(401);
    }
}
