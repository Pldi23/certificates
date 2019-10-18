package com.epam.esm.security;

import com.epam.esm.dto.UserDTO;
import com.epam.esm.entity.Role;
import com.epam.esm.service.LoginUserDetailsService;
import com.epam.esm.service.UserService;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.log4j.Log4j2;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
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
public class OathSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private LoginUserDetailsService detailsService;
    private UserService userService;

    public OathSuccessHandler(LoginUserDetailsService detailsService, UserService userService) {
        this.detailsService = detailsService;
        this.userService = userService;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
//        List<String> roles = authentication.getAuthorities()
//                .stream()
//                .map(GrantedAuthority::getAuthority)
//                .collect(Collectors.toList());

//        DefaultOAuth2User
        OAuth2User user = (OAuth2User) authentication.getPrincipal();
        List<String> roles;
        try {
//            UserDetails userDetails = detailsService.loadUserByUsername(user.getEmail());
            UserDetails userDetails = detailsService.loadUserByUsername((String) user.getAttributes().get("email"));
             roles = userDetails.getAuthorities().stream()
                    .map(GrantedAuthority::getAuthority)
                    .collect(Collectors.toList());

        } catch (UsernameNotFoundException ex) {
            log.warn("please sing up first");
            UserDTO userDTO = UserDTO.builder()
                    .email((String) user.getAttributes().get("email"))
                    .role("ROLE_USER")
                    .password(null)
                    .build();

            roles = List.of(userDTO.getRole());
            UserDTO saved = userService.save(userDTO);
//            response.sendError(401, "unauthorized");
        }
        byte[] signingKey = SecurityConstants.JWT_SECRET.getBytes();

        String token = Jwts.builder()
                .signWith(SignatureAlgorithm.HS512, signingKey)
                .setHeaderParam("typ", SecurityConstants.TOKEN_TYPE)
                .setIssuer(SecurityConstants.TOKEN_ISSUER)
                .setAudience(SecurityConstants.TOKEN_AUDIENCE)
                .setSubject((String) user.getAttributes().get("email"))
                .setExpiration(Date.from(LocalDateTime.now().plusMinutes(10).atZone(ZoneId.systemDefault()).toInstant()))
                .claim("rol", roles)
                .compact();

        log.info("successful authentication, user " + user.getAttributes().get("email") + " role:: " + user.getAuthorities());


        response.addHeader(SecurityConstants.TOKEN_HEADER, SecurityConstants.TOKEN_PREFIX + token);
        response.sendRedirect("/bearer");

    }
}
