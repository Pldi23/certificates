package com.epam.esm.security;


import com.epam.esm.dto.LoginUserPrinciple;
import com.epam.esm.service.LoginUserDetailsService;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

/**
 * gift-certificates
 *
 * @author Dzmitry Platonov on 2019-10-19.
 * @version 0.0.1
 */
@Component
public class SecurityChecker {

    private UserDetailsService userDetailsService;

    public SecurityChecker(LoginUserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    public boolean check(Long id) {
        LoginUserPrinciple principle = (LoginUserPrinciple) userDetailsService.loadUserByUsername((String) SecurityContextHolder.getContext().getAuthentication().getPrincipal());
        return id.equals(principle.getUser().getId());
    }
}
