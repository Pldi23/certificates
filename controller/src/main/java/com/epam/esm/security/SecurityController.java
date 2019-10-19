package com.epam.esm.security;

import com.epam.esm.dto.LoginUserPrinciple;
import com.epam.esm.dto.MessageDTO;
import com.epam.esm.service.LoginUserDetailsService;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.security.RolesAllowed;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;
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
    private LoginUserDetailsService detailsService;

    public SecurityController(TokenCreator tokenCreator, LoginUserDetailsService detailsService) {
        this.tokenCreator = tokenCreator;
        this.detailsService = detailsService;
    }

    @GetMapping(value = "/bearer")
    public ResponseEntity getPublicMessage() {
        return ResponseEntity.ok(new MessageDTO("Hello, you could take your bearer from header", 200));
    }

    @PostMapping("/authenticate/refresh-token")
    public ResponseEntity getTokens(HttpServletRequest request) {
        HttpHeaders httpHeaders = new HttpHeaders();
        String username = request.getParameter("username");
        String refreshToken = request.getHeader(SecurityConstants.REFRESH_TOKEN_HEADER);
        log.info("'/authenticate/refresh-token' authentication attempt by " + username);
        LoginUserPrinciple principle = (LoginUserPrinciple) detailsService.loadUserByUsername(username);
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
            httpHeaders.add(SecurityConstants.TOKEN_HEADER, SecurityConstants.TOKEN_PREFIX + token);
            httpHeaders.add(SecurityConstants.ACCESS_TOKEN_EXPIRATION, String.valueOf(tokenCreator.getJwtTokenExpirationTimestamp(token)));
            httpHeaders.add(SecurityConstants.REFRESH_TOKEN_HEADER, refreshToken);
            return ResponseEntity.ok().headers(httpHeaders).build();
        } else {
            return ResponseEntity.status(401).build();
        }
    }


    @GetMapping(value = "/private")
    public ResponseEntity getPrivateMessage() {
        return ResponseEntity.ok(new MessageDTO("private", 200));
    }

    //    @RolesAllowed("user")
    @Secured({"ROLE_USER"})
    @GetMapping(value = "/user")
    public ResponseEntity getUserMessage() {
        return ResponseEntity.ok(new MessageDTO("for user", 200));
    }

    //    @RolesAllowed("admin")
    @Secured("ROLE_ADMIN")
    @GetMapping(value = "/admin")
    public ResponseEntity getAdminMessage() {
        return ResponseEntity.ok(new MessageDTO("for admin", 200));
    }

    @GetMapping(value = "/")
    public ResponseEntity getEmptyWithHeaderMessage() {
        return ResponseEntity.ok(new MessageDTO("empty", 200));
    }

    @PostMapping(value = "/")
    public ResponseEntity postEmptyWithHeaderMessage() {
        return ResponseEntity.ok(new MessageDTO("post", 200));
    }
}
