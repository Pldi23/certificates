package com.epam.esm.converter;

import com.epam.esm.dto.GiftCertificateDTO;
import com.epam.esm.entity.GiftCertificate;
import org.springframework.stereotype.Component;

/**
 * gift certificates
 *
 * @author Dzmitry Platonov on 2019-09-26.
 * @version 0.0.1
 */
@Component
public class CertificateConverter {

    public GiftCertificateDTO convert(GiftCertificate giftCertificate) {
        return new GiftCertificateDTO(
                giftCertificate.getId(),
                giftCertificate.getName(),
                giftCertificate.getDescription(),
                giftCertificate.getPrice(),
                giftCertificate.getCreationDate(),
                giftCertificate.getModificationDate(),
                giftCertificate.getExpirationDate(),
                giftCertificate.getTags()
        );
    }

    public GiftCertificate convert(GiftCertificateDTO giftCertificateDTO) {
        return new GiftCertificate(
                giftCertificateDTO.getId(),
                giftCertificateDTO.getName(),
                giftCertificateDTO.getDescription(),
                giftCertificateDTO.getPrice(),
                giftCertificateDTO.getCreationDate(),
                giftCertificateDTO.getModificationDate(),
                giftCertificateDTO.getExpirationDate(),
                giftCertificateDTO.getTags()
        );
    }
}
