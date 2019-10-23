package com.epam.esm.security;

import com.epam.esm.dto.ViolationDTO;
import com.epam.esm.util.Translator;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.access.AccessDeniedHandlerImpl;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

/**
 * gift-certificates
 *
 * @author Dzmitry Platonov on 2019-10-18.
 * @version 0.0.1
 */
@Log4j2
public class StatelessCsrfFilter extends OncePerRequestFilter {

    private static final Set<String> SAFE_METHODS = new HashSet<>(Arrays.asList("GET", "HEAD", "TRACE", "OPTIONS"));
    private final AccessDeniedHandler accessDeniedHandler = new AccessDeniedHandlerImpl();

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        if (csrfTokenIsRequired(request)) {

            String csrfHeaderToken = request.getHeader("X-CSRF-TOKEN");
            String csrfCookieToken = null;
            Cookie[] cookies = request.getCookies();
            if (cookies != null) {

                Optional<Cookie> csrfCookie = Arrays.stream(cookies)
                        .filter(cookie -> cookie.getName().equals("X-CSRF-TOKEN"))
                        .findFirst();
                if (csrfCookie.isPresent()) {

                    csrfCookieToken = csrfCookie.get().getValue();
                }
            }
            if (csrfCookieToken == null || !csrfCookieToken.equals(csrfHeaderToken)) {
                log.info("access denied by csrf protection filter");
                log.info(csrfCookieToken + " " + csrfHeaderToken);
                ObjectMapper mapper = new ObjectMapper();
                response.setContentType("application/json;charset=UTF-8");
                response.setStatus(401);
                response.getWriter().write(mapper.writeValueAsString(
                        new ViolationDTO(List.of(Translator.toLocale("violation.csrf")), 401, LocalDateTime.now())));
//                accessDeniedHandler.handle(request, response, new AccessDeniedException("CSRF tokens missing or not matching"));
//                throw new CsrfFilterException(Translator.toLocale("violation.csrf"));
            } else {
                filterChain.doFilter(request, response);

            }
        } else {
            filterChain.doFilter(request, response);

        }
    }

    private boolean csrfTokenIsRequired(HttpServletRequest request) {
        return !SAFE_METHODS.contains(request.getMethod());
    }

}
