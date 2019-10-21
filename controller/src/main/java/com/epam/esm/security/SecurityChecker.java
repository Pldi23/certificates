package com.epam.esm.security;


import com.epam.esm.dto.AppUserPrinciple;
import com.epam.esm.service.AppUserDetailsService;
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

    public SecurityChecker(AppUserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    public boolean check(Long id) {
        AppUserPrinciple principle = (AppUserPrinciple) userDetailsService
                .loadUserByUsername((String) SecurityContextHolder.getContext().getAuthentication().getPrincipal());
        return id.equals(principle.getUser().getId());
    }
}
