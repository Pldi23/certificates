package com.epam.esm.service;

import com.epam.esm.dto.AppUserPrinciple;
import com.epam.esm.entity.Role;
import com.epam.esm.entity.User;
import com.epam.esm.repository.AbstractUserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class AppUserDetailsServiceTest {

    @InjectMocks
    private AppUserDetailsService userDetailsService;

    @Mock
    private AbstractUserRepository userRepository;

    private User user;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        userDetailsService = new AppUserDetailsService(userRepository);

        user = User.builder()
                .id(1L)
                .email("e")
                .role(Role.builder().id(1L).value("ROLE_USER").build())
                .build();
    }

    @Test
    void loadUserByUsername() {

        Mockito.when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.of(user));
        AppUserPrinciple expected = new AppUserPrinciple(user);
        AppUserPrinciple actual = (AppUserPrinciple) userDetailsService.loadUserByUsername(user.getEmail());
        assertEquals(expected, actual);
    }

    @Test
    void update() {
        String token = "token";
        AppUserPrinciple principle = new AppUserPrinciple(user);
        User returned = user;
        returned.setRefreshToken(token);
        Mockito.when(userRepository.save(principle.getUser())).thenReturn(returned);
        AppUserPrinciple expected = new AppUserPrinciple(returned);

        AppUserPrinciple actual = (AppUserPrinciple) userDetailsService.update(principle, token);
        assertEquals(expected, actual);
    }

    @Test
    void updateByEmail() {
        String token = "token";
        User returned = user;
        returned.setRefreshToken(token);
        Mockito.when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.of(user));
        Mockito.when(userRepository.save(user)).thenReturn(returned);
        AppUserPrinciple expected = new AppUserPrinciple(returned);

        AppUserPrinciple actual = (AppUserPrinciple) userDetailsService.update(user.getEmail(), token);
        assertEquals(expected, actual);

    }
}