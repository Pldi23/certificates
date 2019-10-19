package com.epam.esm.security;

import com.epam.esm.service.LoginUserDetailsService;
import com.epam.esm.service.UserService;
import org.springframework.boot.autoconfigure.security.oauth2.resource.PrincipalExtractor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.csrf.CsrfFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;


/**
 * gift-certificates
 *
 * @author Dzmitry Platonov on 2019-10-16.
 * @version 0.0.1
 */
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(
        prePostEnabled = true,
        securedEnabled = true,
        jsr250Enabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private LoginUserDetailsService userDetailsService;
    private UserService userService;
    private TokenCreator tokenCreator;

    public SecurityConfig(LoginUserDetailsService userDetailsService, @Lazy UserService userService, TokenCreator tokenCreator) {
        this.userDetailsService = userDetailsService;
        this.userService = userService;
        this.tokenCreator = tokenCreator;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .and()
                .cors()
                .and()
                .csrf().disable()
                .addFilterBefore(new StatelessCsrfFilter(), CsrfFilter.class)
                .addFilter(new JwtAuthenticationFilter(authenticationManager(), tokenCreator, userDetailsService))
                .addFilter(new JwtAuthorizationFilter(authenticationManager()))
                .addFilter(new OpenIdAuthentificationFilter())
                .authorizeRequests()
                .antMatchers("/", "/authenticate", "/login", "/login/openid", "/error").permitAll()
//                .antMatchers(HttpMethod.GET, "/certificates/**").permitAll()
                .anyRequest()
                .authenticated()
                .and()
//                .addFilter(new RefreshTokenFilter(tokenCreator, userDetailsService))
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .openidLogin().loginProcessingUrl("https://accounts.google.com/o/oauth2/v2/auth?client_id=630015231507-386p2hh0v8ejnsjbh4jhkp2p4earq3vt.apps.googleusercontent.com&response_type=code&scope=openid%20email&redirect_uri=http://localhost:8080/login/openid")
                .successHandler(new OpenIdSuccessHandler(userDetailsService, userService, tokenCreator))
                .failureHandler(new FailedHandler())
                .and()
                .oauth2Login()
                .successHandler(new OpenIdSuccessHandler(userDetailsService, userService, tokenCreator));
    }

    @Override
    public void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService)
                .passwordEncoder(passwordEncoder());
    }

    @Bean
    public AuthenticationFailureHandler authenticationFailureHandler() {
        return new FailedHandler();
    }

    @Bean
    public AppAuthenticationEntryPoint appAuthenticationEntryPoint() {
        return new AppAuthenticationEntryPoint();
    }


    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }


    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", new CorsConfiguration().applyPermitDefaultValues());

        return source;
    }
//
//    @Bean
//    public PrincipalExtractor principalExtractor(LoginUserDetailsService userDetailsService) {
//        return map -> userDetailsService.loadUserByUsername((String) map.get("email"));
//    }


}
