package com.epam.esm.security;

import com.epam.esm.dto.LoginUserPrinciple;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

/**
 * gift-certificates
 *
 * @author Dzmitry Platonov on 2019-10-19.
 * @version 0.0.1
 */
public class RefreshTokenAuthentication implements Authentication {

    private LoginUserPrinciple principle;

    public RefreshTokenAuthentication(LoginUserPrinciple principle) {
        this.principle = principle;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return principle.getAuthorities();
    }

    @Override
    public Object getCredentials() {
        return principle.getUser().getRefreshToken();
    }

    @Override
    public Object getDetails() {
        return null;
    }

    @Override
    public Object getPrincipal() {
        return principle;
    }

    @Override
    public boolean isAuthenticated() {
        return false;
    }

    @Override
    public void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException {

    }

    @Override
    public String getName() {
        return principle.getUsername();
    }
}
