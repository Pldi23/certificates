package com.epam.esm.filter;

import com.epam.esm.constant.RoleConstant;
import com.epam.esm.dto.AppUserPrinciple;
import com.epam.esm.dto.UserDTO;
import com.epam.esm.security.TokenCreator;
import com.epam.esm.service.AppUserDetailsService;
import com.epam.esm.util.CookieUtils;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.epam.esm.filter.HttpCookieOAuth2AuthorizationRequestRepository.REDIRECT_URI_PARAM_COOKIE_NAME;

/**
 * gift-certificates
 *
 * @author Dzmitry Platonov on 2019-11-07.
 * @version 0.0.1
 */
@Component
@Log4j2
public class OAuth2AuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private TokenCreator tokenCreator;

    private HttpCookieOAuth2AuthorizationRequestRepository httpCookieOAuth2AuthorizationRequestRepository;

    private AppUserDetailsService detailsService;


    @Autowired
    OAuth2AuthenticationSuccessHandler(TokenCreator tokenCreator,
                                       HttpCookieOAuth2AuthorizationRequestRepository httpCookieOAuth2AuthorizationRequestRepository,
                                       AppUserDetailsService detailsService) {
        this.tokenCreator = tokenCreator;
        this.httpCookieOAuth2AuthorizationRequestRepository = httpCookieOAuth2AuthorizationRequestRepository;
        this.detailsService = detailsService;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        String targetUrl = determineTargetUrl(request, response, authentication);

        if (response.isCommitted()) {
            logger.debug("Response has already been committed. Unable to redirect to " + targetUrl);
            return;
        }

        clearAuthenticationAttributes(request, response);
        getRedirectStrategy().sendRedirect(request, response, targetUrl);
    }

    protected String determineTargetUrl(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        Optional<String> redirectUri = CookieUtils.getCookie(request, REDIRECT_URI_PARAM_COOKIE_NAME)
                .map(Cookie::getValue);

        String targetUrl = redirectUri.orElse(getDefaultTargetUrl());

//        String token = tokenProvider.createToken(authentication);
        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
        String email = (String) oAuth2User.getAttributes().get("email");
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

        return UriComponentsBuilder.fromUriString(targetUrl)
                .queryParam("token", token)
                .queryParam("refresh", refreshToken)
                .queryParam("expiresIn", String.valueOf(tokenCreator.getJwtTokenExpirationTimestamp(token)))
                .build().toUriString();
    }

    protected void clearAuthenticationAttributes(HttpServletRequest request, HttpServletResponse response) {
        super.clearAuthenticationAttributes(request);
        httpCookieOAuth2AuthorizationRequestRepository.removeAuthorizationRequestCookies(request, response);
    }
}
