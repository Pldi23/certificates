package com.epam.esm.service;

import com.epam.esm.dto.LoginUserPrinciple;
import com.epam.esm.entity.User;
import com.epam.esm.exception.InvalidUserException;
import com.epam.esm.repository.hibernate.EMUserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.AuthenticationUserDetailsService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * gift-certificates
 *
 * @author Dzmitry Platonov on 2019-10-16.
 * @version 0.0.1
 */
@Service
public class LoginUserDetailsService implements UserDetailsService {

    private EMUserRepository userRepository;


    public LoginUserDetailsService(EMUserRepository userRepository) {
        this.userRepository = userRepository;

    }

    @Override
    public UserDetails loadUserByUsername(String username) {
        Optional<User> optionalUser = userRepository.findByEmail(username);
        return new LoginUserPrinciple(optionalUser.orElseThrow(() -> new UsernameNotFoundException("invalid credentials")));

    }

}
