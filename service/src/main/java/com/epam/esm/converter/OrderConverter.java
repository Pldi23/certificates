package com.epam.esm.converter;

import com.epam.esm.dto.GiftCertificateDTO;
import com.epam.esm.dto.OrderDTO;
import com.epam.esm.entity.Order;
import com.epam.esm.entity.OrderCertificate;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * gift-certificates
 *
 * @author Dzmitry Platonov on 2019-10-12.
 * @version 0.0.1
 */
@Component
public class OrderConverter {

    private CertificateConverter certificateConverter;

    public OrderConverter(CertificateConverter certificateConverter) {
        this.certificateConverter = certificateConverter;
    }

    public OrderDTO convert(Order order) {
        List<OrderCertificate> orderCertificates = order.getOrderCertificates();
        List<GiftCertificateDTO> giftCertificateDTOS = orderCertificates == null || orderCertificates.isEmpty() ? new ArrayList<>() :
                orderCertificates.stream()
                .map(orderCertificate -> certificateConverter.convert(orderCertificate.getCertificate()))
                .collect(Collectors.toList());

        return OrderDTO.builder()
                .id(order.getId())
                .createdAt(order.getCreatedAt())
                .userEmail(order.getUser().getEmail())
                .giftCertificates(giftCertificateDTOS)
                .userId(order.getUser().getId())
                .build();
    }
}
