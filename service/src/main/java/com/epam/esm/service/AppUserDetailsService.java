package com.epam.esm.service;

import com.epam.esm.constant.RoleConstant;
import com.epam.esm.dto.AppUserPrinciple;
import com.epam.esm.dto.UserDTO;
import com.epam.esm.entity.Role;
import com.epam.esm.entity.User;
import com.epam.esm.exception.EntityAlreadyExistsException;
import com.epam.esm.repository.AbstractUserRepository;
import com.epam.esm.util.Translator;
import lombok.extern.log4j.Log4j2;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Log4j2
public class AppUserDetailsService implements UserDetailsService {

    private AbstractUserRepository userRepository;


    public AppUserDetailsService(AbstractUserRepository userRepository) {
        this.userRepository = userRepository;

    }

    @Override
    public UserDetails loadUserByUsername(String username) {
        Optional<User> optionalUser = userRepository.findByEmail(username);
        return new AppUserPrinciple(optionalUser.orElseThrow(() -> new UsernameNotFoundException(Translator.toLocale("exception.credentials"))));

    }

    @Transactional
    public UserDetails update(AppUserPrinciple principle, String refreshToken) {
        if (principle != null) {
            User user = principle.getUser();
            user.setRefreshToken(refreshToken);
            return new AppUserPrinciple(userRepository.save(user));
        } else {
            throw new UsernameNotFoundException(Translator.toLocale("exception.invalid.principle"));
        }
    }

    @Transactional
    public UserDetails update(String email, String refreshToken) {
        AppUserPrinciple principle = (AppUserPrinciple) loadUserByUsername(email);
        if (principle != null) {
            User user = principle.getUser();
            user.setRefreshToken(refreshToken);
            return new AppUserPrinciple(userRepository.save(user));
        } else {
            throw new UsernameNotFoundException(Translator.toLocale("exception.invalid.principle"));
        }
    }

    @Transactional
    public void save(UserDTO userDTO) {
        try {
            userRepository.save(User.builder().email(userDTO.getEmail()).role(Role.builder().id(2L).value(RoleConstant.ROLE_USER).build()).build());
        } catch (DataIntegrityViolationException ex) {
            throw new EntityAlreadyExistsException(String.format(Translator.toLocale("exception.user.exist"), userDTO.getEmail()));
        }
    }
}
