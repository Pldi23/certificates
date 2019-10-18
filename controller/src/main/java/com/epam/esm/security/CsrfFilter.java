package com.epam.esm.security;

import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.WebUtils;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * gift-certificates
 *
 * @author Dzmitry Platonov on 2019-10-18.
 * @version 0.0.1
 */
public class CsrfFilter  {
//    @Override
//    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
//        CsrfToken csrf = (CsrfToken) request.getAttribute(CsrfToken.class.getName());
//        System.out.println("csrf :: " + csrf);
//        if (csrf != null) {
//            Cookie cookie = WebUtils.getCookie(request, "XSRF-TOKEN");
//            String token = csrf.getToken();
//            if (cookie == null || token != null && !token.equals(cookie.getValue())) {
//                cookie = new Cookie("XSRF-TOKEN", token);
//                cookie.setPath("/");
//                response.addCookie(cookie);
//            }
//        }
//        filterChain.doFilter(request, response);
//    }
}
