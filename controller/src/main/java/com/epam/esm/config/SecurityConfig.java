package com.epam.esm.config;

import com.epam.esm.filter.AppAccessDeniedHandler;
import com.epam.esm.filter.ExceptionHandlerFilter;
import com.epam.esm.filter.JwtAuthorizationFilter;
import com.epam.esm.filter.OpenIdSuccessHandler;
import com.epam.esm.filter.StatelessCsrfFilter;
import com.epam.esm.security.TokenCreator;
import com.epam.esm.service.AppUserDetailsService;
import com.epam.esm.service.UserService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
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

    private AppUserDetailsService userDetailsService;
    private UserService userService;
    private TokenCreator tokenCreator;

    public SecurityConfig(AppUserDetailsService userDetailsService, @Lazy UserService userService, TokenCreator tokenCreator) {
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
                .exceptionHandling()
                .accessDeniedHandler(new AppAccessDeniedHandler())
                .and()
                .csrf().disable()
                .addFilterBefore(new StatelessCsrfFilter(), CsrfFilter.class)
                .addFilter(new JwtAuthorizationFilter(authenticationManager()))
                .addFilterBefore(new ExceptionHandlerFilter(), JwtAuthorizationFilter.class)
                .authorizeRequests()
                .antMatchers("/", "/authenticate", "/authenticate/refresh-token", "/login", "/error").permitAll()
                .antMatchers(HttpMethod.GET, "/certificates").permitAll()
                .antMatchers(HttpMethod.POST, "/users").permitAll()
                .anyRequest()
                .authenticated()
                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
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
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }


    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", new CorsConfiguration().applyPermitDefaultValues());

        return source;
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }


}
