package com.epam.esm.validate;


import com.epam.esm.entity.GiftCertificate;

import java.util.List;

public interface Validator {

    void validate(List<GiftCertificate> giftCertificates);
}
