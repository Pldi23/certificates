package com.epam.esm.security;

import com.epam.esm.dto.LoginUserPrinciple;
import com.epam.esm.dto.UserDTO;
import com.epam.esm.service.LoginUserDetailsService;
import com.epam.esm.service.UserService;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

/**
 * gift-certificates
 *
 * @author Dzmitry Platonov on 2019-10-17.
 * @version 0.0.1
 */
@Log4j2
@Component
public class OpenIdSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private LoginUserDetailsService detailsService;
    private UserService userService;
    private TokenCreator tokenCreator;

    public OpenIdSuccessHandler(LoginUserDetailsService detailsService, UserService userService, TokenCreator tokenCreator) {
        this.detailsService = detailsService;
        this.userService = userService;
        this.tokenCreator = tokenCreator;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        OAuth2User user = (OAuth2User) authentication.getPrincipal();
        String email = (String) user.getAttributes().get("email");
        List<String> roles;
        LoginUserPrinciple principle;
        try {
            principle = (LoginUserPrinciple) detailsService.loadUserByUsername(email);
             roles = principle.getAuthorities().stream()
                    .map(GrantedAuthority::getAuthority)
                    .collect(Collectors.toList());

        } catch (UsernameNotFoundException ex) {
            log.warn("singing up Oath2 user: " + email);
            UserDTO userDTO = UserDTO.builder()
                    .email(email)
                    .role("ROLE_USER")
                    .password(null)
                    .build();

            roles = List.of(userDTO.getRole());
            userService.save(userDTO);
        }

        String token = tokenCreator.createJwt(email, roles);
        String refreshToken = tokenCreator.createRefreshToken(email, roles);

        detailsService.update(email, refreshToken);

        log.info("successful authentication, user " + user.getAttributes().get("email") + " role:: " + user.getAuthorities());


        response.addHeader(SecurityConstants.TOKEN_HEADER, SecurityConstants.TOKEN_PREFIX + token);
        response.addHeader(SecurityConstants.ACCESS_TOKEN_EXPIRATION, String.valueOf(tokenCreator.getJwtTokenExpirationTimestamp(token)));
        response.addHeader(SecurityConstants.REFRESH_TOKEN_HEADER, refreshToken);
//        response.sendRedirect("/bearer");

    }
}
