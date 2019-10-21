package com.epam.esm.service;

import com.epam.esm.converter.OrderConverter;
import com.epam.esm.dto.GiftCertificateDTO;
import com.epam.esm.dto.OrderDTO;
import com.epam.esm.dto.OrderSearchCriteriaDTO;
import com.epam.esm.dto.PageAndSortDTO;
import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.entity.Order;
import com.epam.esm.entity.OrderCertificate;
import com.epam.esm.repository.EMCertificateRepository;
import com.epam.esm.repository.EMOrderCertificateRepository;
import com.epam.esm.repository.EMOrderRepository;
import com.epam.esm.repository.EMUserRepository;
import com.epam.esm.util.Translator;
import lombok.extern.log4j.Log4j2;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * gift-certificates
 *
 * @author Dzmitry Platonov on 2019-10-12.
 * @version 0.0.1
 */
@Service
@Log4j2
public class OrderServiceImpl implements OrderService {

    private EMOrderRepository orderRepository;
    private EMCertificateRepository certificateRepository;
    private EMUserRepository userRepository;
    private OrderConverter orderConverter;
    private EMOrderCertificateRepository orderCertificateRepository;

    public OrderServiceImpl(EMOrderRepository orderRepository, EMCertificateRepository certificateRepository,
                            EMUserRepository userRepository, OrderConverter orderConverter,
                            EMOrderCertificateRepository orderCertificateRepository) {
        this.orderRepository = orderRepository;
        this.certificateRepository = certificateRepository;
        this.userRepository = userRepository;
        this.orderConverter = orderConverter;
        this.orderCertificateRepository = orderCertificateRepository;
    }

    @Transactional
    @Override
    public void delete(long id) {
        try {
            orderCertificateRepository.deleteByOrderId(id);
            orderRepository.deleteById(id);
        } catch (EmptyResultDataAccessException ex) {
            throw new EntityNotFoundException(String.format(Translator.toLocale("{entity.order.not.found}"), id));
        }
    }

    @Override
    public List<OrderDTO> findAll(PageAndSortDTO pageAndSortDTO) {
        return orderRepository.findAll(pageAndSortDTO.getSortParameter(), pageAndSortDTO.getPage(), pageAndSortDTO.getSize()).stream()
                .map(order -> orderConverter.convert(order))
                .peek(orderDTO -> orderDTO.setPrice(orderCertificateRepository.calculateOrderFixedPrice(orderDTO.getId())))
                .collect(Collectors.toList());
    }

    @Override
    public OrderDTO findOne(long id) {
        Order order = orderRepository.findById(id).orElseThrow(() -> new EntityNotFoundException(String.format(Translator.toLocale("{entity.order.not.found}"), id)));
        OrderDTO orderDTO = orderConverter.convert(order);
        orderDTO.setPrice(orderCertificateRepository.calculateOrderFixedPrice(id));
        return orderDTO;
    }

    @Transactional
    @Override
    public OrderDTO save(OrderDTO orderDTO) {
        Order saved = orderRepository.save(Order.builder()
                .createdAt(LocalDateTime.now())
                .user(userRepository.findByEmail(orderDTO.getUserEmail()).orElseThrow(() -> new EntityNotFoundException(String.format(Translator.toLocale("{entity.user.not.found}"), orderDTO.getUserEmail()))))
                .build());
        saveOrderCertificates(saved, orderDTO.getGiftCertificates());

        saved.setOrderCertificates(new HashSet<>(orderCertificateRepository.findAllByOrderId(saved.getId())));

        OrderDTO dto = orderConverter.convert(saved);
        dto.setPrice(orderCertificateRepository.calculateOrderFixedPrice(saved.getId()));
        return dto;
    }

    @Transactional
    @Override
    public OrderDTO update(OrderDTO orderDTO, long id) {
        Optional<Order> optionalOrder = orderRepository.findById(id);
        if (optionalOrder.isPresent()) {
            Order order = optionalOrder.get();
            orderCertificateRepository.deleteByOrderId(id);
            saveOrderCertificates(order, orderDTO.getGiftCertificates());
            Order updated = orderRepository.save(order);
            updated.setOrderCertificates(new HashSet<>(orderCertificateRepository.findAllByOrderId(updated.getId())));
            OrderDTO dto = orderConverter.convert(updated);
            dto.setPrice(orderCertificateRepository.calculateOrderFixedPrice(updated.getId()));
            return dto;
        } else {
            throw new EntityNotFoundException(String.format(Translator.toLocale("{entity.order.not.found}"), id));
        }
    }

    @Override
    public List<OrderDTO> findByCriteria(OrderSearchCriteriaDTO criteriaDTO, PageAndSortDTO pageAndSortDTO) {

        return orderRepository.findByCriteria(pageAndSortDTO.getSortParameter(), pageAndSortDTO.getPage(),
                pageAndSortDTO.getSize(), criteriaDTO.getEmail(), criteriaDTO.getUserId(),
                criteriaDTO.getCertificatesNames(), criteriaDTO.getCertificatesIds()).stream()
                .map(order -> orderConverter.convert(order))
                .peek(orderDTO -> {
                    BigDecimal price = orderCertificateRepository.calculateOrderFixedPrice(orderDTO.getId());
                    orderDTO.setPrice(price != null ? price : new BigDecimal(0));
                })
                .collect(Collectors.toList());
    }



    private void saveOrderCertificates(Order order, Set<GiftCertificateDTO> giftCertificates) {
        Set<GiftCertificate> certificates = giftCertificates.stream().map(giftCertificateDTO -> giftCertificateDTO.getId() == null ?
                certificateRepository.findByName(giftCertificateDTO.getName()).orElseThrow(() ->
                        new EntityNotFoundException(String.format(Translator.toLocale("{certificate.not.found.by.name}"), giftCertificateDTO.getName()))) :
                certificateRepository.findById(giftCertificateDTO.getId(), false).orElseThrow(() ->
                        new EntityNotFoundException(String.format(Translator.toLocale("{entity.certificate.not.found}"), giftCertificateDTO.getId()))))
                .collect(Collectors.toSet());


        certificates.forEach(certificate -> orderCertificateRepository.save(OrderCertificate.builder()
                .order(order)
                .certificate(certificate)
                .fixedPrice(certificate.getPrice())
                .expirationDate(certificate.getExpirationDate())
                .build()));
    }


}
