package com.epam.esm.service;

import com.epam.esm.converter.UserConverter;
import com.epam.esm.dto.PageAndSortDTO;
import com.epam.esm.dto.UserDTO;
import com.epam.esm.dto.UserPatchDTO;
import com.epam.esm.entity.Role;
import com.epam.esm.entity.User;
import com.epam.esm.repository.AbstractUserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.internal.verification.VerificationModeFactory.times;


class UserServiceImplTest {


    @InjectMocks
    private UserServiceImpl userService;

    @Mock
    private AbstractUserRepository userRepository;

    private UserConverter userConverter;

    @Mock
    private PasswordEncoder passwordEncoder;

    private UserDTO userDTO;
    private User user;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        userConverter = new UserConverter();
        userService = new UserServiceImpl(userRepository, userConverter, passwordEncoder);

        user = User.builder()
                .id(1L)
                .email("e")
                .role(Role.builder().id(1L).value("ROLE_USER").build())
                .build();

        userDTO = UserDTO.builder()
                .id(1L)
                .email("e")
                .role("ROLE_USER")
                .ordersIds(new ArrayList<>())
                .build();
    }


    @Test
    void delete() {
        userService.delete(user.getId());
        Mockito.verify(userRepository, times(1)).deleteById(user.getId());
    }

    @Test
    void findAll() {

        List<User> certificates = List.of(user);
        PageAndSortDTO pageAndSortDTO = PageAndSortDTO.builder().sortParameter(null).page(1).size(Integer.MAX_VALUE).build();

        Mockito.when(userRepository
                .findAll(pageAndSortDTO.getSortParameter(), pageAndSortDTO.getPage(), pageAndSortDTO.getSize()))
                .thenReturn(certificates);

        List<UserDTO> expected = List.of(userDTO);

        assertEquals(expected, userService.findAll(pageAndSortDTO));
    }

    @Test
    void findOne() {

        Mockito.when(userRepository.findById(1)).thenReturn(Optional.of(user));

        UserDTO expected = userDTO;
        UserDTO actual = userService.findOne(user.getId());
        Mockito.verify(userRepository, times(1)).findById(1);
        assertEquals(expected, actual);
    }

    @Test
    void patch() {
        UserPatchDTO patchDTO = UserPatchDTO.builder().email("em").password("p").role("ROLE_ADMIN").build();
        User patched = User.builder().id(1L).email("em").password("p").role(Role.builder().id(1L).value("ROLE_ADMIN").build()).build();
        Mockito.when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
        Mockito.when(userRepository.save(any())).thenReturn(patched);


        UserDTO actual = userService.patch(patchDTO, user.getId());
        UserDTO expected = UserDTO.builder().id(1L).email("em").password("p").role("ROLE_ADMIN").ordersIds(new ArrayList<>()).build();
        assertEquals(expected, actual);
    }

    @Test
    void save() {

        Mockito.when(userRepository.save(any())).thenReturn(user);

        UserDTO actual = userService.save(userDTO);
        assertEquals(userDTO, actual);
    }

    @Test
    void update() {
        Mockito.when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
        Mockito.when(userRepository.save(any())).thenReturn(user);

        UserDTO actual = userService.update(userDTO, user.getId());
        assertEquals(userDTO, actual);

    }

    @Test
    void findByEmail() {

        Mockito.when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.of(user));

        Optional<User> expected = Optional.of(user);
        Optional<User> actual = userService.findByEmail(user.getEmail());
        Mockito.verify(userRepository, times(1)).findByEmail(user.getEmail());
        assertEquals(expected, actual);
    }
}