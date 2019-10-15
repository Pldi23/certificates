package com.epam.esm.service;

import com.epam.esm.converter.OrderConverter;
import com.epam.esm.dto.OrderDTO;
import com.epam.esm.dto.OrderSearchCriteriaDTO;
import com.epam.esm.dto.PageAndSortDTO;
import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.entity.Order;
import com.epam.esm.entity.User;
import com.epam.esm.repository.hibernate.EMCertificateRepository;
import com.epam.esm.repository.hibernate.EMOrderRepository;
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
                .peek(orderDTO -> orderDTO.setPrice(orderRepository.calculatePrice(orderDTO.getId())))
                .collect(Collectors.toList());
    }

    @Override
    public OrderDTO findOne(long id) {
        Order order = orderRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("order not found"));
        OrderDTO orderDTO = orderConverter.convert(order);
        orderDTO.setPrice(orderRepository.calculatePrice(id));
        return orderDTO;
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
        OrderDTO dto = orderConverter.convert(orderRepository.save(order));
        dto.setPrice(orderRepository.calculatePrice(dto.getId()));
        return dto;
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
            OrderDTO dto = orderConverter.convert(orderRepository.save(order));
            dto.setPrice(orderRepository.calculatePrice(id));
            return dto;
        } else {
            throw new EntityNotFoundException("order not found");
        }
    }

    @Override
    public List<OrderDTO> findByCriteria(OrderSearchCriteriaDTO criteriaDTO, PageAndSortDTO pageAndSortDTO) {

        return orderRepository.findByCriteria(pageAndSortDTO.getSortParameter(), pageAndSortDTO.getPage(),
                pageAndSortDTO.getSize(), criteriaDTO.getEmail(), criteriaDTO.getUserId(),
                criteriaDTO.getCertificatesNames(), criteriaDTO.getCertificatesIds()).stream()
                .map(order -> orderConverter.convert(order))
                .peek(orderDTO -> orderDTO.setPrice(orderRepository.calculatePrice(orderDTO.getId())))
                .collect(Collectors.toList());
    }
}
