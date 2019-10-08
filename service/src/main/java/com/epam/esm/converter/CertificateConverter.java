package com.epam.esm.converter;

import com.epam.esm.dto.GiftCertificateDTO;
import com.epam.esm.dto.TagDTO;
import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.entity.Tag;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * gift certificates
 *
 * @author Dzmitry Platonov on 2019-09-26.
 * @version 0.0.1
 */
@Component
public class CertificateConverter {

    public GiftCertificateDTO convert(GiftCertificate giftCertificate) {
        return new GiftCertificateDTO.Builder()
                .withId(giftCertificate.getId())
                .withName(giftCertificate.getName())
                .withDescription(giftCertificate.getDescription())
                .withPrice(giftCertificate.getPrice())
                .withCreationDate(giftCertificate.getCreationDate())
                .withModificationDate(giftCertificate.getModificationDate())
                .withExpirationDate(giftCertificate.getExpirationDate())
                .withTags(giftCertificate.getTags().stream().filter(Objects::nonNull).map(tag ->
                        new TagDTO(tag.getId(), tag.getTitle())).collect(Collectors.toSet()))
                .build();
    }

    public GiftCertificate convert(GiftCertificateDTO giftCertificateDTO) {
        Set<Tag> tags = giftCertificateDTO.getTags() != null ?
                giftCertificateDTO.getTags().stream().filter(Objects::nonNull).map(tagDTO ->
                        new Tag(tagDTO.getId(), tagDTO.getTitle())).collect(Collectors.toSet()) :
                new HashSet<>();
        return new GiftCertificate.Builder()
                .withId(giftCertificateDTO.getId() != null ? giftCertificateDTO.getId() : 0L)
                .withName(giftCertificateDTO.getName())
                .withDescription(giftCertificateDTO.getDescription())
                .withPrice(giftCertificateDTO.getPrice())
                .withCreationDate(giftCertificateDTO.getCreationDate())
                .withModificationDate(giftCertificateDTO.getModificationDate())
                .withExpirationDate(giftCertificateDTO.getExpirationDate())
                .withTags(tags)
                .build();
    }
}
