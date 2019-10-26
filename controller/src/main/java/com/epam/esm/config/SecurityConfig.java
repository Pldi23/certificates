package com.epam.esm.config;

import com.epam.esm.filter.AppAccessDeniedHandler;
import com.epam.esm.filter.ExceptionHandlerFilter;
import com.epam.esm.filter.JwtAuthorizationFilter;
import com.epam.esm.filter.OpenIdSuccessHandler;
import com.epam.esm.filter.StatelessCsrfFilter;
import com.epam.esm.security.TokenCreator;
import com.epam.esm.service.AppUserDetailsService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
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


@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(
        prePostEnabled = true,
        securedEnabled = true,
        jsr250Enabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private AppUserDetailsService userDetailsService;

    public SecurityConfig(AppUserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .and()
                .cors()
                .and()
                .exceptionHandling()
                .accessDeniedHandler(appAccessDeniedHandler())
                .and()
                .csrf().disable()
                .addFilterBefore(statelessCsrfFilter(), CsrfFilter.class)
                .addFilter(jwtAuthorizationFilter(authenticationManagerBean()))
                .addFilterBefore(exceptionHandlerFilter(), JwtAuthorizationFilter.class)
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
                .successHandler(openIdSuccessHandler(userDetailsService, tokenCreator()));
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

    @Bean
    public JwtAuthorizationFilter jwtAuthorizationFilter(AuthenticationManager authenticationManager) {
        return new JwtAuthorizationFilter(authenticationManager);
    }

    @Bean
    public ExceptionHandlerFilter exceptionHandlerFilter() {
        return new ExceptionHandlerFilter();
    }

    @Bean
    public StatelessCsrfFilter statelessCsrfFilter() {
        return new StatelessCsrfFilter();
    }

    @Bean
    public AppAccessDeniedHandler appAccessDeniedHandler() {
        return new AppAccessDeniedHandler();
    }

    @Bean
    public TokenCreator tokenCreator() {
        return new TokenCreator();
    }

    @Bean
    public OpenIdSuccessHandler openIdSuccessHandler(AppUserDetailsService appUserDetailsService, TokenCreator tokenCreator) {
        return new OpenIdSuccessHandler(appUserDetailsService, tokenCreator);
    }

}
