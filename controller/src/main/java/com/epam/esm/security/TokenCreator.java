package com.epam.esm.security;

import com.epam.esm.constant.SecurityConstant;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.stereotype.Component;

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
@Component
public class TokenCreator {

    public String createJwt(String username, List<String> roles) {
        return SecurityConstant.TOKEN_PREFIX + Jwts.builder()
                .signWith(SignatureAlgorithm.HS512, SecurityConstant.JWT_SECRET.getBytes())
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
                .signWith(SignatureAlgorithm.HS512, SecurityConstant.JWT_SECRET.getBytes())
                .setSubject(username)
                .setExpiration(Date.from(LocalDateTime.now().plusDays(SecurityConstant.REFRESH_TOKEN_DURATION_DAYS).atZone(ZoneId.systemDefault()).toInstant()))
                .claim("rol", roles)
                .compact();

    }

    public long getJwtTokenExpirationTimestamp(String token) {
        Jws<Claims> parsedToken = Jwts.parser()
                .setSigningKey(SecurityConstant.JWT_SECRET.getBytes())
                .parseClaimsJws(token.replace(SecurityConstant.TOKEN_PREFIX, ""));

        return parsedToken.getBody().getExpiration().getTime();
    }
}
