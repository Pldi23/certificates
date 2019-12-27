package com.epam.esm.validate;


import com.epam.esm.entity.GiftCertificate;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class CertificateValidator implements Validator {

    private static final String CERTIFICATE_NAME_REGEX_PATTERN = "([\\w-]+(?: [\\w-]+)+)|([\\w-]+)";
    private static final int CERTIFICATE_DESCRIPTION_MAX_LENGTH = 1000;

    @Override
    public void validate(List<GiftCertificate> giftCertificates) {
        giftCertificates.forEach(this::checkCertificate);
    }

    private void checkCertificate(GiftCertificate giftCertificate) {
        checkPrice(giftCertificate);
        checkName(giftCertificate);
        checkDescription(giftCertificate);
    }

    private void checkPrice(GiftCertificate giftCertificate) {
        if (giftCertificate.getPrice().intValue() < 0) {
            throw new ValidationException("negative price detected");
        }
    }

    private void checkName(GiftCertificate giftCertificate) {
        if (!giftCertificate.getName().matches(CERTIFICATE_NAME_REGEX_PATTERN)) {
            throw new ValidationException("wrong name detected");
        }
    }

    private void checkDescription(GiftCertificate giftCertificate) {
        if (giftCertificate.getDescription() != null && giftCertificate.getDescription().length() > CERTIFICATE_DESCRIPTION_MAX_LENGTH) {
            throw new ValidationException("wrong name detected");
        }
    }

}
