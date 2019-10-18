package com.epam.esm.service;

import com.epam.esm.converter.UserConverter;
import com.epam.esm.dto.PageAndSortDTO;
import com.epam.esm.dto.UserDTO;
import com.epam.esm.dto.UserPatchDTO;
import com.epam.esm.entity.User;
import com.epam.esm.entity.UserDetails;
import com.epam.esm.exception.EntityAlreadyExistsException;
import com.epam.esm.repository.hibernate.EMUserRepository;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * gift-certificates
 *
 * @author Dzmitry Platonov on 2019-10-12.
 * @version 0.0.1
 */
@Service
public class UserServiceImpl implements UserService {

    private EMUserRepository userRepository;
    private UserConverter userConverter;
    private PasswordEncoder passwordEncoder;

    public UserServiceImpl(EMUserRepository userRepository, UserConverter userConverter, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.userConverter = userConverter;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    @Override
    public void delete(long id) {
        try {
            userRepository.deleteById(id);
        } catch (EmptyResultDataAccessException ex) {
            throw new EntityNotFoundException("user not found");
        }
    }

    @Override
    public List<UserDTO> findAll(PageAndSortDTO pageAndSortDTO) {
        return userRepository.findAll(pageAndSortDTO.getSortParameter(), pageAndSortDTO.getPage(), pageAndSortDTO.getSize()).stream()
                .map(user -> userConverter.convert(user))
                .collect(Collectors.toList());
    }

    @Override
    public UserDTO findOne(long id) {
        return userConverter.convert(userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("user not found")));
    }

    @Transactional
    @Override
    public UserDTO patch(UserPatchDTO userPatchDTO, Long id) {
        User user = userRepository.findById(id).orElseThrow(()
                -> new EntityNotFoundException("user not found"));
        if (userPatchDTO.getEmail() != null) {
            user.setEmail(userPatchDTO.getEmail());
        }
        if (userPatchDTO.getPassword() != null) {
            user.setPassword(userPatchDTO.getPassword());
        }
        if (userPatchDTO.getRole() != null) {
            user.setRole(userConverter.convertRole(userPatchDTO.getRole()));
        }
        try {
            return userConverter.convert(userRepository.save(user));
        } catch (DataIntegrityViolationException ex) {
            throw new EntityAlreadyExistsException("User with email '" + userPatchDTO.getEmail() + "' already exists");
        }
    }

    @Transactional
    @Override
    public UserDTO save(UserDTO userDTO) {
        User user = userConverter.convert(userDTO);
        if (user.getPassword() != null) {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
        }
        User saved;
        try {
            saved = userRepository.save(user);
        } catch (DataIntegrityViolationException ex) {
            throw new EntityAlreadyExistsException("User with email '" + userDTO.getEmail() + "' already exists");
        }
        return userConverter.convert(saved);
    }

    @Transactional
    @Override
    public UserDTO update(UserDTO userDTO, long id) {
        if (userRepository.findById(id).isPresent()) {
            User user = userConverter.convert(userDTO);
            user.setId(id);
            try {
                return userConverter.convert(userRepository.save(user));
            } catch (DataIntegrityViolationException ex) {
                throw new EntityAlreadyExistsException("User with email '" + userDTO.getEmail() + "' already exists");
            }
        } else {
            throw new EntityNotFoundException("user not found");
        }
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }
}
