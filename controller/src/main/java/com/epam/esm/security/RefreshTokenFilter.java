package com.epam.esm.security;

import com.epam.esm.dto.LoginUserPrinciple;
import com.epam.esm.service.LoginUserDetailsService;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

/**
 * gift-certificates
 *
 * @author Dzmitry Platonov on 2019-10-19.
 * @version 0.0.1
 */
@Log4j2
public class RefreshTokenFilter extends UsernamePasswordAuthenticationFilter {

    private TokenCreator tokenCreator;
    private LoginUserDetailsService detailsService;

    public RefreshTokenFilter(TokenCreator tokenCreator,
                              LoginUserDetailsService detailsService) {
        this.tokenCreator = tokenCreator;
        this.detailsService = detailsService;
        setFilterProcessesUrl(SecurityConstants.AUTH_REFRESH_TOKEN_URL);
    }

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) res;
        String username = request.getParameter("username");
        String refreshToken = request.getHeader(SecurityConstants.REFRESH_TOKEN_HEADER);
        log.info("'/authenticate/refresh-token' authentication attempt by " + username);
        if (username != null && refreshToken != null) {
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
                response.addHeader(SecurityConstants.TOKEN_HEADER, SecurityConstants.TOKEN_PREFIX + token);
                response.addHeader(SecurityConstants.ACCESS_TOKEN_EXPIRATION, String.valueOf(tokenCreator.getJwtTokenExpirationTimestamp(token)));
                response.addHeader(SecurityConstants.REFRESH_TOKEN_HEADER, refreshToken);
                chain.doFilter(request, response);
                return;
            }

        }
        chain.doFilter(req, res);
    }

//    @Override
//    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
//        String username = request.getParameter("username");
//        log.info("'/authenticate/refresh-token' authentication attempt by " + username);
//        String refreshToken = request.getHeader(SecurityConstants.REFRESH_TOKEN_HEADER);
//        LoginUserPrinciple userDetails = (LoginUserPrinciple) detailsService.loadUserByUsername(username);
//        if (userDetails != null && userDetails.getUser() != null && userDetails.getUser().getRefreshToken() != null
//                && userDetails.getUser().getRefreshToken().equals(refreshToken)) {
//            String pass = userDetails.getUser().getPassword();
//            log.info("pass " + userDetails.getUser().getPassword());
//            UsernamePasswordAuthenticationToken authenticationToken =
//                    new UsernamePasswordAuthenticationToken(username, null);
//
//            log.info("'/authenticate/refresh-token' refresh token equals " + username);
//            return authenticationManager.authenticate(authenticationToken);
//        }
//        log.info("'/authenticate/refresh-token' authentication null for " + username);
//        UsernamePasswordAuthenticationToken authenticationToken =
//                new UsernamePasswordAuthenticationToken(username, null);
//        return authenticationManager.authenticate(authenticationToken);
//    }
//
//    @Override
//    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response,
//                                            FilterChain filterChain, Authentication authentication) {
//        LoginUserPrinciple user = ((LoginUserPrinciple) authentication.getPrincipal());
//        List<String> roles = user.getAuthorities()
//                .stream()
//                .map(GrantedAuthority::getAuthority)
//                .collect(Collectors.toList());
//
//        String token = tokenCreator.createJwt(user.getUsername(), roles);
//        String refreshToken = tokenCreator.createRefreshToken(user.getUsername(), roles);
//        detailsService.update(user, refreshToken);
//        log.info("successful authentication, user " + user.getUsername() + " role:: " + user.getAuthorities());
//        response.addHeader(SecurityConstants.TOKEN_HEADER, SecurityConstants.TOKEN_PREFIX + token);
//        response.addHeader(SecurityConstants.ACCESS_TOKEN_EXPIRATION, String.valueOf(tokenCreator.getJwtTokenExpirationTimestamp(token)));
//        response.addHeader(SecurityConstants.REFRESH_TOKEN_HEADER, refreshToken);
//    }
//
//    @Override
//    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response,
//                                              AuthenticationException failed) {
//        log.warn("unsuccessful authentication because of " + failed.getMessage());
//        response.setStatus(401);
//    }

}
