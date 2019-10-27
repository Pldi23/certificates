package com.epam.esm.security;

import com.epam.esm.constant.SecurityConstant;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

/**
 * to create unique jwt
 *
 * @author Dzmitry Platonov on 2019-10-18.
 * @version 0.0.1
 */
@PropertySource("app.properties")
public class TokenCreator {

    @Value("${jwt.secret}")
    private String jwtSecret;

    public String createJwt(String username, List<String> roles) {
        return SecurityConstant.TOKEN_PREFIX + Jwts.builder()
                .signWith(SignatureAlgorithm.HS512, jwtSecret.getBytes())
                .setHeaderParam("typ", SecurityConstant.TOKEN_TYPE)
                .setIssuer(SecurityConstant.TOKEN_ISSUER)
                .setAudience(SecurityConstant.TOKEN_AUDIENCE)
                .setSubject(username)
                .setExpiration(Date.from(LocalDateTime.now().plusHours(SecurityConstant.JWT_TOKEN_DURATION_HOURS).atZone(ZoneId.systemDefault()).toInstant()))
                .claim("rol", roles)
                .compact();
    }

    public String createRefreshToken(String username, List<String> roles) {
        return Jwts.builder()
                .signWith(SignatureAlgorithm.HS512, jwtSecret.getBytes())
                .setSubject(username)
                .setExpiration(Date.from(LocalDateTime.now().plusDays(SecurityConstant.REFRESH_TOKEN_DURATION_DAYS).atZone(ZoneId.systemDefault()).toInstant()))
                .claim("rol", roles)
                .compact();

    }

    public long getJwtTokenExpirationTimestamp(String token) {
        Jws<Claims> parsedToken = Jwts.parser()
                .setSigningKey(jwtSecret.getBytes())
                .parseClaimsJws(token.replace(SecurityConstant.TOKEN_PREFIX, ""));

        return parsedToken.getBody().getExpiration().getTime();
    }
}
