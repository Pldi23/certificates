package com.epam.esm.converter;

import com.epam.esm.dto.GiftCertificateDTO;
import com.epam.esm.dto.OrderDTO;
import com.epam.esm.dto.TagDTO;
import com.epam.esm.dto.UserDTO;
import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.entity.Order;
import com.epam.esm.entity.Tag;
import com.epam.esm.entity.User;
import org.springframework.stereotype.Component;

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

    public OrderConverter(UserConverter userConverter) {
        this.userConverter = userConverter;
    }

    public OrderDTO convert(Order order) {
//        return OrderDTO.builder()
//                .id(order.getId())
//                .createdAt(order.getCreatedAt())
////                .user(UserDTO.builder()
////                        .id(order.getUser().getId())
////                        .email(order.getUser().getEmail())
////                        .password(order.getUser().getPassword())
////                        .role(order.getUser().getRole().getValue())
////                        .build())
//                .user(userConverter.convert(order.getUser()))

//                .build();
        return OrderDTO.builder()
                .id(order.getId())
                .createdAt(order.getCreatedAt())
                .userEmail(order.getUser().getEmail())
                .giftCertificates(order.getGiftCertificates().stream()
                        .map(giftCertificate -> new GiftCertificateDTO.Builder()
                                .withId(giftCertificate.getId())
                                .withName(giftCertificate.getName())
                                .withDescription(giftCertificate.getName())
                                .withPrice(giftCertificate.getPrice())
                                .withCreationDate(giftCertificate.getCreationDate())
                                .withModificationDate(giftCertificate.getModificationDate())
                                .withExpirationDate(giftCertificate.getExpirationDate())
                                .withTags(giftCertificate.getTags().stream().map(tag -> TagDTO.builder()
                                        .id(tag.getId())
                                        .title(tag.getTitle())
                                        .build()).collect(Collectors.toSet()))
                                .build()).collect(Collectors.toSet()))
//                .giftCertificatesNames(order.getGiftCertificates().stream()
//                        .map(GiftCertificate::getName)
//                        .collect(Collectors.toSet()))
                .certificatesIds(order.getGiftCertificates().stream()
                        .map(GiftCertificate::getId).collect(Collectors.toList()))
                .userId(order.getUser().getId())
                .build();
    }
}
