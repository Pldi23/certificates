package com.epam.esm.filter;

import com.epam.esm.constant.RoleConstant;
import com.epam.esm.constant.SecurityConstant;
import com.epam.esm.dto.AppUserPrinciple;
import com.epam.esm.dto.UserDTO;
import com.epam.esm.security.TokenCreator;
import com.epam.esm.service.AppUserDetailsService;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.stream.Collectors;

/**
 * to handle oath success authentication
 *
 * @author Dzmitry Platonov on 2019-10-17.
 * @version 0.0.1
 */
@Log4j2
public class OpenIdSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private static final String OATH_EMAIL_ATTRIBUTE = "email";

    private AppUserDetailsService detailsService;
    private TokenCreator tokenCreator;

    public OpenIdSuccessHandler(AppUserDetailsService detailsService, TokenCreator tokenCreator) {
        this.detailsService = detailsService;
        this.tokenCreator = tokenCreator;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
        String email = (String) oAuth2User.getAttributes().get(OATH_EMAIL_ATTRIBUTE);
        List<String> roles;
        AppUserPrinciple principle;
        try {
            principle = (AppUserPrinciple) detailsService.loadUserByUsername(email);
             roles = principle.getAuthorities().stream()
                    .map(GrantedAuthority::getAuthority)
                    .collect(Collectors.toList());

        } catch (UsernameNotFoundException ex) {
            log.info("singing up Oath2 user: " + email);
            UserDTO userDTO = UserDTO.builder()
                    .email(email)
                    .role(RoleConstant.ROLE_USER)
                    .password(null)
                    .build();

            roles = List.of(userDTO.getRole());
            detailsService.save(userDTO);
        }

        String token = tokenCreator.createJwt(email, roles);
        String refreshToken = tokenCreator.createRefreshToken(email, roles);

        detailsService.update(email, refreshToken);

        log.info("successful authentication, user " + oAuth2User.getAttributes().get(OATH_EMAIL_ATTRIBUTE)
                + " role:: " + oAuth2User.getAuthorities());


        response.addHeader(SecurityConstant.TOKEN_HEADER, token);
        response.addHeader(SecurityConstant.ACCESS_TOKEN_EXPIRATION, String.valueOf(tokenCreator.getJwtTokenExpirationTimestamp(token)));
        response.addHeader(SecurityConstant.REFRESH_TOKEN_HEADER, refreshToken);
    }
}
