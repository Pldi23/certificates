package com.epam.esm.repository;

import com.epam.esm.entity.GiftCertificate;

import java.util.List;

/**
 * gift certificates
 *
 * @author Dzmitry Platonov on 2019-09-28.
 * @version 0.0.1
 */
public interface AbstractCertificateRepository extends Repository<GiftCertificate> {

    List<GiftCertificate> getCertificatesByNameOrDescription(String description, String name);
}
