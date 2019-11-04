package com.epam.esm.filter;

import com.epam.esm.constant.EndPointConstant;
import com.epam.esm.constant.SecurityConstant;
import com.epam.esm.dto.ViolationDTO;
import com.epam.esm.util.Translator;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
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
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * authorization by Jwt
 *
 * @author Dzmitry Platonov on 2019-10-16.
 * @version 0.0.1
 */
@Log4j2
@PropertySource("app.properties")
public class JwtAuthorizationFilter extends BasicAuthenticationFilter {


    private static final Map<String, String> SAFE_ENDPOINTS =
            Map.of("/", "GET",
                    EndPointConstant.AUTHENTICATE_ENDPOINT, "POST",
                    EndPointConstant.REFRESH_TOKEN_ENDPOINT, "POST",
                    EndPointConstant.LOGIN_ENDPOINT, "GET",
                    EndPointConstant.ERROR_ENDPOINT, "GET",
                    EndPointConstant.CERTIFICATE_ENDPOINT, "GET",
                    EndPointConstant.CERTIFICATE_ID_ENDPOINT, "GET",
                    EndPointConstant.CERTIFICATE_ENDPOINT + "/", "GET",
                    EndPointConstant.USER_ENDPOINT, "POST");

    @Value("${jwt.secret}")
    private String jwtSecret;

    public JwtAuthorizationFilter(AuthenticationManager authenticationManager) {
        super(authenticationManager);
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain filterChain) throws IOException, ServletException {
        UsernamePasswordAuthenticationToken authentication = getAuthentication(request);
        if (authentication == null && !verifyNullableToken(request)) {
            ObjectMapper mapper = new ObjectMapper();
            response.setContentType("application/json;charset=UTF-8");
            response.setStatus(401);
            response.getWriter().write(mapper.writeValueAsString(
                    new ViolationDTO(List.of(Translator.toLocale("violation.autorization")), LocalDateTime.now())));
        } else if (authentication == null) {
            filterChain.doFilter(request, response);
        } else {
            SecurityContextHolder.getContext().setAuthentication(authentication);
            filterChain.doFilter(request, response);

        }
    }

    private UsernamePasswordAuthenticationToken getAuthentication(HttpServletRequest request) {
        String token = request.getHeader(SecurityConstant.TOKEN_HEADER);
        if (token != null && !token.isEmpty() && token.startsWith(SecurityConstant.TOKEN_PREFIX)) {

            Jws<Claims> parsedToken = Jwts.parser()
                    .setSigningKey(jwtSecret.getBytes())
                    .parseClaimsJws(token.replace(SecurityConstant.TOKEN_PREFIX, ""));

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
                return new UsernamePasswordAuthenticationToken(username, null, authorities);
            }
        }
        return null;
    }

    private boolean verifyNullableToken(HttpServletRequest request) {
        String uri = request.getRequestURI();
        uri = uri.replaceFirst("certificates\\/\\d+$", "certificates/");
        log.info(uri);
        return SAFE_ENDPOINTS.containsKey(uri) && SAFE_ENDPOINTS.get(uri).equals(request.getMethod());
    }

}
