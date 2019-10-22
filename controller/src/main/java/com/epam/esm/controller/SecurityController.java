package com.epam.esm.controller;

import com.epam.esm.dto.AppUserPrinciple;
import com.epam.esm.security.SecurityConstants;
import com.epam.esm.security.TokenCreator;
import com.epam.esm.service.AppUserDetailsService;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.stream.Collectors;

/**
 * gift-certificates
 *
 * @author Dzmitry Platonov on 2019-10-17.
 * @version 0.0.1
 */
@RestController
@Log4j2
public class SecurityController {

    private TokenCreator tokenCreator;
    private AppUserDetailsService detailsService;
    private AuthenticationManager authenticationManager;

    public SecurityController(TokenCreator tokenCreator, AppUserDetailsService detailsService, AuthenticationManager authenticationManager) {
        this.tokenCreator = tokenCreator;
        this.detailsService = detailsService;
        this.authenticationManager = authenticationManager;
    }

    @PostMapping("/authenticate/refresh-token")
    public ResponseEntity getTokens(HttpServletRequest request) {
        HttpHeaders httpHeaders = new HttpHeaders();
        String username = request.getParameter("username");
        String refreshToken = request.getHeader(SecurityConstants.REFRESH_TOKEN_HEADER);
        log.info("'/authenticate/refresh-token' authentication attempt by " + username);
        AppUserPrinciple principle = (AppUserPrinciple) detailsService.loadUserByUsername(username);
        if (principle != null && principle.getUser() != null && principle.getUser().getRefreshToken() != null
                && principle.getUser().getRefreshToken().equals(refreshToken)) {
            log.info("'/authenticate/refresh-token' refresh token equals " + username);
            List<String> roles = principle.getAuthorities()
                    .stream()
                    .map(GrantedAuthority::getAuthority)
                    .collect(Collectors.toList());

            String token = tokenCreator.createJwt(principle.getUsername(), roles);
            String updatedRefreshToken = tokenCreator.createRefreshToken(principle.getUsername(), roles);
            detailsService.update(principle, updatedRefreshToken);
            httpHeaders.add(SecurityConstants.TOKEN_HEADER, token);
            httpHeaders.add(SecurityConstants.ACCESS_TOKEN_EXPIRATION, String.valueOf(tokenCreator.getJwtTokenExpirationTimestamp(token)));
            httpHeaders.add(SecurityConstants.REFRESH_TOKEN_HEADER, refreshToken);
            return ResponseEntity.ok().headers(httpHeaders).build();
        } else {
            return ResponseEntity.status(401).build();
        }
    }

    @PostMapping("/authenticate")
    public ResponseEntity authenticate(HttpServletRequest request) {
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(username, password);
        log.info("'/authenticate' authentication attempt by " + username);
        Authentication authenticate = authenticationManager.authenticate(authenticationToken);
        HttpHeaders httpHeaders = new HttpHeaders();
        AppUserPrinciple user = ((AppUserPrinciple) authenticate.getPrincipal());
        List<String> roles = user.getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());

        String token = tokenCreator.createJwt(user.getUsername(), roles);
        String refreshToken = tokenCreator.createRefreshToken(user.getUsername(), roles);
        detailsService.update(user, refreshToken);
        log.info("successful authentication, user " + user.getUsername() + " role:: " + user.getAuthorities());
        httpHeaders.add(SecurityConstants.TOKEN_HEADER, token);
        httpHeaders.add(SecurityConstants.ACCESS_TOKEN_EXPIRATION, String.valueOf(tokenCreator.getJwtTokenExpirationTimestamp(token)));
        httpHeaders.add(SecurityConstants.REFRESH_TOKEN_HEADER, refreshToken);
        return ResponseEntity.ok().headers(httpHeaders).build();

    }
}

