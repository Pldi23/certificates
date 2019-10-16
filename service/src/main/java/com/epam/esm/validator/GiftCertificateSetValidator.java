package com.epam.esm.validator;

import com.epam.esm.dto.GiftCertificateDTO;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Set;

/**
 * to validate set of {@link GiftCertificateDTO} when it comes nested in {@link com.epam.esm.dto.OrderDTO}
 * to be saved inside service layer
 *
 * @author Dzmitry Platonov on 2019-10-13.
 * @version 0.0.1
 */
public class GiftCertificateSetValidator implements ConstraintValidator<ValidCertificatesSet, Set<GiftCertificateDTO>> {

    @Override
    public boolean isValid(Set<GiftCertificateDTO> giftCertificateDTOS, ConstraintValidatorContext constraintValidatorContext) {
        return giftCertificateDTOS.stream()
                .allMatch(giftCertificateDTO -> isValidFields(giftCertificateDTO) && isValidId(giftCertificateDTO)
                        && isValidName(giftCertificateDTO));

    }

    private boolean isValidId(GiftCertificateDTO giftCertificateDTO) {
        if (giftCertificateDTO.getId() != null) {
            return giftCertificateDTO.getId() > 0;
        }
        return true;
    }

    private boolean isValidName(GiftCertificateDTO giftCertificateDTO) {
        if (giftCertificateDTO.getName() != null) {
            return !giftCertificateDTO.getName().isBlank()
                    && giftCertificateDTO.getName().matches("([\\w-]+(?: [\\w-]+)+)|([\\w-]+)")
                    && giftCertificateDTO.getName().length() >= 1
                    && giftCertificateDTO.getName().length() <= 30;
        }
        return true;
    }

    private boolean isValidFields(GiftCertificateDTO giftCertificateDTO) {
        if (giftCertificateDTO.getId() == null) {
            return giftCertificateDTO.getName() != null;
        }
        if (giftCertificateDTO.getName() == null) {
            return giftCertificateDTO.getId() != null;
        }
        return false;
    }
}
