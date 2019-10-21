package com.epam.esm.converter;

import com.epam.esm.dto.GiftCertificateDTO;
import com.epam.esm.dto.OrderDTO;
import com.epam.esm.entity.Order;
import com.epam.esm.entity.OrderCertificate;
import com.epam.esm.repository.AbstractOrderRepository;
import com.epam.esm.repository.AbstractUserRepository;
import org.springframework.stereotype.Component;

import javax.persistence.EntityNotFoundException;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * gift-certificates
 *
 * @author Dzmitry Platonov on 2019-10-12.
 * @version 0.0.1
 */
@Component
public class OrderConverter {

    private UserConverter userConverter;
    private AbstractUserRepository userRepository;
    private AbstractOrderRepository orderRepository;
    private CertificateConverter certificateConverter;

    public OrderConverter(UserConverter userConverter, AbstractUserRepository userRepository,
                          AbstractOrderRepository orderRepository, CertificateConverter certificateConverter) {
        this.userConverter = userConverter;
        this.userRepository = userRepository;
        this.orderRepository = orderRepository;
        this.certificateConverter = certificateConverter;
    }

    public Order convert(OrderDTO orderDTO) {
        Set<OrderCertificate> orderCertificates = orderDTO.getGiftCertificates().stream()
                .map(giftCertificateDTO -> OrderCertificate.builder()
                        .order(orderRepository.findById(orderDTO.getId()).orElseThrow(() -> new EntityNotFoundException("user not found")))
                        .certificate(certificateConverter.convert(giftCertificateDTO))
                        .fixedPrice(orderDTO.getPrice())
                        .build())
                .collect(Collectors.toSet());
        return Order.builder()
                .id(orderDTO.getId())
                .user(userRepository.findById(orderDTO.getUserId()).orElseThrow(() -> new EntityNotFoundException("user not found")))
                .orderCertificates(orderCertificates)
                .createdAt(orderDTO.getCreatedAt())
                .build();
    }

    public OrderDTO convert(Order order) {
        Set<OrderCertificate> orderCertificates = order.getOrderCertificates();
        Set<GiftCertificateDTO> giftCertificateDTOS = orderCertificates == null || orderCertificates.isEmpty() ? new HashSet<>() :
                orderCertificates.stream()
                .map(orderCertificate -> certificateConverter.convert(orderCertificate.getCertificate()))
                .collect(Collectors.toSet());

        return OrderDTO.builder()
                .id(order.getId())
                .createdAt(order.getCreatedAt())
                .userEmail(order.getUser().getEmail())
                .giftCertificates(giftCertificateDTOS)
                .build();
    }
}
