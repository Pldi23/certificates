package com.epam.esm.controller;

import com.epam.esm.constant.EndPointConstant;
import com.epam.esm.constant.RequestConstant;
import com.epam.esm.constant.SecurityConstant;
import com.epam.esm.dto.AppUserPrinciple;
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
 * login and refresh-token endpoints
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

    @PostMapping(EndPointConstant.REFRESH_TOKEN_ENDPOINT)
    public ResponseEntity getTokens(HttpServletRequest request) {
        String username = request.getParameter(RequestConstant.USERNAME);
        String refreshToken = request.getHeader(SecurityConstant.REFRESH_TOKEN_HEADER);
        AppUserPrinciple principle = (AppUserPrinciple) detailsService.loadUserByUsername(username);
        if (principle != null && principle.getUser() != null && principle.getUser().getRefreshToken() != null
                && principle.getUser().getRefreshToken().equals(refreshToken)) {
            return ResponseEntity.ok().headers(buildHeaders(principle)).build();
        } else {
            return ResponseEntity.status(401).build();
        }
    }

    @PostMapping(EndPointConstant.AUTHENTICATE_ENDPOINT)
    public ResponseEntity authenticate(HttpServletRequest request) {
        String username = request.getParameter(RequestConstant.USERNAME);
        String password = request.getParameter(RequestConstant.PASS);
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(username, password);
        log.info("authentication attempt by " + username);
        Authentication authenticate = authenticationManager.authenticate(authenticationToken);
        AppUserPrinciple user = ((AppUserPrinciple) authenticate.getPrincipal());
        log.info("successful authentication, user " + user.getUsername() + " role:: " + user.getAuthorities());
        return ResponseEntity.ok().headers(buildHeaders(user)).build();

    }

    private HttpHeaders buildHeaders(AppUserPrinciple principle) {
        HttpHeaders httpHeaders = new HttpHeaders();
        List<String> roles = principle.getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());
        String token = tokenCreator.createJwt(principle.getUsername(), roles);
        String refreshToken = tokenCreator.createRefreshToken(principle.getUsername(), roles);
        detailsService.update(principle, refreshToken);
        httpHeaders.add(SecurityConstant.TOKEN_HEADER, token);
        httpHeaders.add(SecurityConstant.ACCESS_TOKEN_EXPIRATION, String.valueOf(tokenCreator.getJwtTokenExpirationTimestamp(token)));
        httpHeaders.add(SecurityConstant.REFRESH_TOKEN_HEADER, refreshToken);
        return httpHeaders;
    }
}

