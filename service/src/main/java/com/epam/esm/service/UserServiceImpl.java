package com.epam.esm.service;

import com.epam.esm.converter.UserConverter;
import com.epam.esm.dto.PageAndSortDTO;
import com.epam.esm.dto.UserDTO;
import com.epam.esm.dto.UserPatchDTO;
import com.epam.esm.entity.User;
import com.epam.esm.exception.EntityAlreadyExistsException;
import com.epam.esm.repository.AbstractUserRepository;
import com.epam.esm.util.Translator;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Service
public class UserServiceImpl implements UserService {

    private AbstractUserRepository userRepository;
    private UserConverter userConverter;
    private PasswordEncoder passwordEncoder;

    public UserServiceImpl(AbstractUserRepository userRepository, UserConverter userConverter, PasswordEncoder passwordEncoder) {
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
            throw new EntityNotFoundException(String.format(Translator.toLocale("entity.user.not.found.id"), id));
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
                .orElseThrow(() -> new EntityNotFoundException(String.format(Translator.toLocale("entity.user.not.found.id"), id))));
    }

    @Transactional
    @Override
    public UserDTO patch(UserPatchDTO userPatchDTO, Long id) {
        User user = userRepository.findById(id).orElseThrow(()
                -> new EntityNotFoundException(String.format(Translator.toLocale("entity.user.not.found.id"), id)));
        if (userPatchDTO.getEmail() != null) {
            user.setEmail(userPatchDTO.getEmail());
        }
        if (userPatchDTO.getPassword() != null) {
            user.setPassword(passwordEncoder.encode(userPatchDTO.getPassword()));
        }
        if (userPatchDTO.getRole() != null) {
            user.setRole(userConverter.convertRole(userPatchDTO.getRole()));
        }
        try {
            return userConverter.convert(userRepository.save(user));
        } catch (DataIntegrityViolationException ex) {
            throw new EntityAlreadyExistsException(String.format(Translator.toLocale("exception.user.exist"), userPatchDTO.getEmail()));
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
            throw new EntityAlreadyExistsException(String.format(Translator.toLocale("exception.user.exist"), userDTO.getEmail()));
        }
        return userConverter.convert(saved);
    }

    @Transactional
    @Override
    public UserDTO update(UserDTO userDTO, long id) {
        if (userRepository.findById(id).isPresent()) {
            User user = userConverter.convert(userDTO);
            user.setId(id);
            if (userDTO.getPassword() != null) {
                user.setPassword(passwordEncoder.encode(userDTO.getPassword()));
            }
            try {
                return userConverter.convert(userRepository.save(user));
            } catch (DataIntegrityViolationException ex) {
                throw new EntityAlreadyExistsException(String.format(Translator.toLocale("exception.user.exist"), userDTO.getEmail()));
            }
        } else {
            throw new EntityNotFoundException(String.format(Translator.toLocale("entity.user.not.found.id"), id));
        }
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

}
