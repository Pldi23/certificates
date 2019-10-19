package com.epam.esm.service;

import com.epam.esm.dto.LoginUserPrinciple;
import com.epam.esm.entity.User;
import com.epam.esm.repository.hibernate.EMUserRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * gift-certificates
 *
 * @author Dzmitry Platonov on 2019-10-16.
 * @version 0.0.1
 */
@Service
@Log4j2
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

    @Transactional
    public UserDetails update(LoginUserPrinciple principle, String refreshToken) {
        if (principle != null) {
            User user = principle.getUser();
            user.setRefreshToken(refreshToken);
            return new LoginUserPrinciple(userRepository.save(user));
        } else {
            throw new UsernameNotFoundException("invalid principle provided");
        }
    }

    @Transactional
    public UserDetails update(String email, String refreshToken) {
        LoginUserPrinciple principle = (LoginUserPrinciple) loadUserByUsername(email);
        if (principle != null) {
            User user = principle.getUser();
            user.setRefreshToken(refreshToken);
            return new LoginUserPrinciple(userRepository.save(user));
        } else {
            throw new UsernameNotFoundException("invalid principle provided");
        }
    }
}
