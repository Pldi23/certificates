package com.epam.esm.security;

import com.epam.esm.dto.ViolationDTO;
import com.epam.esm.util.Translator;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * gift-certificates
 *
 * @author Dzmitry Platonov on 2019-10-16.
 * @version 0.0.1
 */
@Log4j2
public class JwtAuthorizationFilter extends BasicAuthenticationFilter {

    private static final Map<String, String> SAFE_ENDPOINTS =
            Map.of("/", "GET",
                    "/authenticate", "POST",
                    "/authenticate/refresh-token", "POST",
                    "/login", "GET",
                    "/error", "GET",
                    "/certificates", "GET",
                    "/users", "POST");

    public JwtAuthorizationFilter(AuthenticationManager authenticationManager) {
        super(authenticationManager);
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain filterChain) throws IOException, ServletException {
        log.info("authorization");
        UsernamePasswordAuthenticationToken authentication = getAuthentication(request);
        if (authentication == null && !verifyNullToken(request)) {
            ObjectMapper mapper = new ObjectMapper();
            response.setContentType("application/json;charset=UTF-8");
            response.setStatus(401);
            response.getWriter().write(mapper.writeValueAsString(
                    new ViolationDTO(List.of(Translator.toLocale("violation.autorization")), 401, LocalDateTime.now())));
        } else if (authentication == null) {
            log.info("authentication is null ");
            filterChain.doFilter(request, response);
        } else {
            SecurityContextHolder.getContext().setAuthentication(authentication);
            filterChain.doFilter(request, response);

        }
    }

    private UsernamePasswordAuthenticationToken getAuthentication(HttpServletRequest request) {
        String token = request.getHeader(SecurityConstants.TOKEN_HEADER);
        if (token != null && !token.isEmpty() && token.startsWith(SecurityConstants.TOKEN_PREFIX)) {

            Jws<Claims> parsedToken = Jwts.parser()
                    .setSigningKey(SecurityConstants.JWT_SECRET.getBytes())
                    .parseClaimsJws(token.replace("Bearer ", ""));

            String username = parsedToken
                    .getBody()
                    .getSubject();
            log.info(username);

            LocalDateTime expiration =
                    LocalDateTime.ofInstant(parsedToken.getBody().getExpiration().toInstant(), ZoneId.systemDefault());


            List<SimpleGrantedAuthority> authorities = ((List<?>) parsedToken.getBody()
                    .get("rol")).stream()
                    .map(authority -> new SimpleGrantedAuthority((String) authority))
                    .collect(Collectors.toList());

            log.info(username + " authorized as " + authorities);

            if (username != null && !username.isEmpty() && expiration.isAfter(LocalDateTime.now())) {
                log.info("to verify principle");
                return new UsernamePasswordAuthenticationToken(username, null, authorities);
            }

        }

        return null;
    }

    private boolean verifyNullToken(HttpServletRequest request) {
        String uri = request.getRequestURI();
        log.info(uri);
        boolean result = SAFE_ENDPOINTS.containsKey(uri) && SAFE_ENDPOINTS.get(uri).equals(request.getMethod());
        log.info(result);
        return result;
    }

}
