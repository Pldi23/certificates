package com.epam.esm.config;

import com.epam.esm.filter.AppAccessDeniedHandler;
import com.epam.esm.filter.ExceptionHandlerFilter;
import com.epam.esm.filter.HttpCookieOAuth2AuthorizationRequestRepository;
import com.epam.esm.filter.JwtAuthorizationFilter;
import com.epam.esm.filter.OAuth2AuthenticationFailureHandler;
import com.epam.esm.filter.OAuth2AuthenticationSuccessHandler;
import com.epam.esm.security.TokenCreator;
import com.epam.esm.service.AppUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
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
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import java.util.Arrays;

import static com.epam.esm.constant.EndPointConstant.*;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(
        prePostEnabled = true,
        securedEnabled = true,
        jsr250Enabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private AppUserDetailsService userDetailsService;

    @Autowired
    private OAuth2AuthenticationSuccessHandler oAuth2AuthenticationSuccessHandler;

    @Autowired
    private OAuth2AuthenticationFailureHandler oAuth2AuthenticationFailureHandler;

    public SecurityConfig(AppUserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .cors()
                .and()
                .authorizeRequests()
                .and()
                .exceptionHandling()
                .accessDeniedHandler(appAccessDeniedHandler())
                .and()
                .csrf()
                .disable()
//                .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
//                .and()
                .addFilter(jwtAuthorizationFilter(authenticationManagerBean()))
                .addFilterBefore(exceptionHandlerFilter(), JwtAuthorizationFilter.class)
                .authorizeRequests()
                .antMatchers("/", AUTHENTICATE_ENDPOINT, REFRESH_TOKEN_ENDPOINT, LOGIN_ENDPOINT, ERROR_ENDPOINT, "/oauth2/**").permitAll()
                .antMatchers(HttpMethod.GET, CERTIFICATE_ENDPOINT).permitAll()
                .antMatchers(HttpMethod.GET, CERTIFICATE_ID_ENDPOINT).permitAll()
                .antMatchers(HttpMethod.POST, USER_ENDPOINT).permitAll()
                .anyRequest()
                .authenticated()
                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .formLogin().successForwardUrl(AUTHENTICATE_ENDPOINT)
                .and()
                .oauth2Login()
                .authorizationEndpoint()
                .baseUri("/oauth2/authorize")
                .authorizationRequestRepository(cookieAuthorizationRequestRepository())
                .and()
                .redirectionEndpoint()
                .baseUri("/oauth2/callback/*")
                .and()
                .failureHandler(oAuth2AuthenticationFailureHandler)
                .successHandler(oAuth2AuthenticationSuccessHandler);
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
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true);
        config.setAllowedOrigins(Arrays.asList("https://localhost:3000","http://localhost:3000"));
        config.setAllowedMethods(Arrays.asList("POST", "OPTIONS", "GET", "DELETE", "PUT", "PATCH"));
        config.setAllowedHeaders(Arrays.asList("X-Requested-With", "Origin", "Content-Type", "Accept", "Authorization",
                "X-XSRF-TOKEN", "Access-Control-Allow-Headers", "RefreshToken"));
        config.setExposedHeaders(Arrays.asList("Authorization", "RefreshToken", "ExpiresIn"));
        source.registerCorsConfiguration("/**", config);
        FilterRegistrationBean bean = new FilterRegistrationBean(new CorsFilter(source));
        bean.setOrder(Ordered.HIGHEST_PRECEDENCE);
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
    public AppAccessDeniedHandler appAccessDeniedHandler() {
        return new AppAccessDeniedHandler();
    }

    @Bean
    public TokenCreator tokenCreator() {
        return new TokenCreator();
    }


    @Bean
    public HttpCookieOAuth2AuthorizationRequestRepository cookieAuthorizationRequestRepository() {
        return new HttpCookieOAuth2AuthorizationRequestRepository();
    }

}
