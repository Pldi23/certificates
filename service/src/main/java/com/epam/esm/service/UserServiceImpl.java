package com.epam.esm.service;

import com.epam.esm.converter.UserConverter;
import com.epam.esm.dto.PageAndSortDTO;
import com.epam.esm.dto.PageableList;
import com.epam.esm.dto.UserDTO;
import com.epam.esm.dto.UserPatchDTO;
import com.epam.esm.entity.Order;
import com.epam.esm.entity.User;
import com.epam.esm.exception.EntityAlreadyExistsException;
import com.epam.esm.repository.AbstractOrderCertificateRepository;
import com.epam.esm.repository.AbstractOrderRepository;
import com.epam.esm.repository.AbstractUserRepository;
import com.epam.esm.repository.page.PageSizeData;
import com.epam.esm.repository.predicate.OrderHasUserIdSpecification;
import com.epam.esm.repository.sort.UserSortData;
import com.epam.esm.util.Translator;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.stream.Collectors;


@Service
public class UserServiceImpl implements UserService {

    private static final String USER_NOT_FOUND_MESSAGE = "entity.user.not.found.id";
    private static final String USER_EXIST_MESSAGE = "exception.user.exist";

    private AbstractUserRepository userRepository;
    private UserConverter userConverter;
    private PasswordEncoder passwordEncoder;
    private AbstractOrderCertificateRepository orderCertificateRepository;
    private AbstractOrderRepository orderRepository;

    public UserServiceImpl(AbstractUserRepository userRepository, UserConverter userConverter, PasswordEncoder passwordEncoder,
                           AbstractOrderCertificateRepository orderCertificateRepository, AbstractOrderRepository orderRepository) {
        this.userRepository = userRepository;
        this.userConverter = userConverter;
        this.passwordEncoder = passwordEncoder;
        this.orderCertificateRepository = orderCertificateRepository;
        this.orderRepository = orderRepository;
    }

    @Transactional
    @Override
    public void delete(long id) {
        try {
            OrderHasUserIdSpecification orderHasUserIdSpecification = new OrderHasUserIdSpecification(id);
            List<Order> orders = orderRepository.findAllSpecified(List.of(orderHasUserIdSpecification), null, new PageSizeData(1, Integer.MAX_VALUE));
            orders.forEach(order -> orderCertificateRepository.deleteByOrderId(order.getId()));
            userRepository.deleteById(id);
        } catch (EmptyResultDataAccessException ex) {
            throw new EntityNotFoundException(String.format(Translator.toLocale(USER_NOT_FOUND_MESSAGE), id));
        }
    }

    @Override
    public PageableList<UserDTO> findAll(PageAndSortDTO pageAndSortDTO) {
        return PageableList.<UserDTO>builder().list(userRepository.findAllSpecified(null,
                pageAndSortDTO.getSortParameter() != null ? new UserSortData(pageAndSortDTO.getSortParameter()) : null,
                new PageSizeData(pageAndSortDTO.getPage(), pageAndSortDTO.getSize())).stream()
                .map(user -> userConverter.convert(user)).collect(Collectors.toList()))
                .lastPage(userRepository.countLastPage(pageAndSortDTO.getSize()))
                .build();
    }

    @Override
    public UserDTO findOne(long id) {
        return userConverter.convert(userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(String.format(Translator.toLocale(USER_NOT_FOUND_MESSAGE), id))));
    }

    @Transactional
    @Override
    public UserDTO patch(UserPatchDTO userPatchDTO, Long id) {
        User user = userRepository.findById(id).orElseThrow(()
                -> new EntityNotFoundException(String.format(Translator.toLocale(USER_NOT_FOUND_MESSAGE), id)));
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
            throw new EntityAlreadyExistsException(String.format(Translator.toLocale(USER_EXIST_MESSAGE), userPatchDTO.getEmail()));
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
            throw new EntityAlreadyExistsException(String.format(Translator.toLocale(USER_EXIST_MESSAGE), userDTO.getEmail()));
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
                throw new EntityAlreadyExistsException(String.format(Translator.toLocale(USER_EXIST_MESSAGE), userDTO.getEmail()));
            }
        } else {
            throw new EntityNotFoundException(String.format(Translator.toLocale(USER_NOT_FOUND_MESSAGE), id));
        }
    }
}
