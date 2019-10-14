package com.epam.esm.service;

import com.epam.esm.converter.OrderConverter;
import com.epam.esm.dto.OrderDTO;
import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.entity.Order;
import com.epam.esm.entity.User;
import com.epam.esm.repository.hibernate.EMCertificateRepository;
import com.epam.esm.repository.hibernate.EMOrderRepository;
import com.epam.esm.repository.jpa.CertificateRepository;
import com.epam.esm.repository.jpa.OrderRepository;
import com.epam.esm.repository.jpa.UserRepository;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * gift-certificates
 *
 * @author Dzmitry Platonov on 2019-10-12.
 * @version 0.0.1
 */
@Service
public class OrderServiceImpl implements OrderService {

    private EMOrderRepository orderRepository;
    private EMCertificateRepository certificateRepository;
    private UserRepository userRepository;
    private OrderConverter orderConverter;

    public OrderServiceImpl(EMOrderRepository orderRepository, EMCertificateRepository certificateRepository,
                            UserRepository userRepository, OrderConverter orderConverter) {
        this.orderRepository = orderRepository;
        this.certificateRepository = certificateRepository;
        this.userRepository = userRepository;
        this.orderConverter = orderConverter;
    }

    @Transactional
    @Override
    public void delete(long id) {
        try {
            orderRepository.deleteById(id);
        } catch (EmptyResultDataAccessException ex) {
            throw new EntityNotFoundException("order not found");
        }
    }

    @Override
    public List<OrderDTO> findAll() {
        return orderRepository.findAll().stream()
                .map(order -> orderConverter.convert(order))
                .collect(Collectors.toList());
    }

    @Override
    public OrderDTO findOne(long id) {
        Order order = orderRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("order not found"));
        return orderConverter.convert(order);
    }

    @Transactional
    @Override
    public OrderDTO save(OrderDTO orderDTO) {
        User user = userRepository.findByEmail(orderDTO.getUserEmail()).orElseThrow(() -> new EntityNotFoundException("user not found"));
        Set<GiftCertificate> giftCertificates = orderDTO.getGiftCertificatesNames().stream()
                .map(name -> certificateRepository.findByName(name).orElseThrow(() -> new EntityNotFoundException("certificate '" + name + "' not found")))
                .collect(Collectors.toSet());
        Order order = Order.builder()
                .createdAt(LocalDateTime.now())
                .user(user)
                .giftCertificates(giftCertificates)
                .build();
        return orderConverter.convert(orderRepository.save(order));
    }

    @Transactional
    @Override
    public OrderDTO update(OrderDTO orderDTO, long id) {
        if (orderRepository.findById(id).isPresent()) {
            Order order = Order.builder()
                    .id(id)
                    .createdAt(LocalDateTime.now())
                    .user(userRepository.findByEmail(orderDTO.getUserEmail()).orElseThrow(() -> new EntityNotFoundException("user not found")))
                    .giftCertificates(orderDTO.getGiftCertificatesNames().stream()
                            .map(name -> certificateRepository.findByName(name).orElseThrow(() -> new EntityNotFoundException("certificate '" + name + "' not found")))
                            .collect(Collectors.toSet()))
                    .build();
            return orderConverter.convert(orderRepository.save(order));
        } else {
            throw new EntityNotFoundException("order not found");
        }
    }
}
