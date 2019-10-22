package com.epam.esm.security;

import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.openid.OpenIDAuthenticationFilter;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;

/**
 * gift-certificates
 *
 * @author Dzmitry Platonov on 2019-10-19.
 * @version 0.0.1
 */
@Log4j2
public class OpenIdAuthentificationFilter extends OpenIDAuthenticationFilter {

    public OpenIdAuthentificationFilter() {
        setFilterProcessesUrl("/login/openid");
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        log.info(request.getUserPrincipal());
//        request.getParameterMap().forEach((k,v) -> log.info("key :: " + k + " value :: " + Arrays.toString(v)));

        return null;
    }
}
