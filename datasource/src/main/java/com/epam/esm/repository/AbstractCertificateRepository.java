package com.epam.esm.repository;

import com.epam.esm.entity.GiftCertificate;

import java.util.Optional;


public interface AbstractCertificateRepository extends SaveRepository<GiftCertificate>, FindAllSpecifiedRepository<GiftCertificate>,
        RemoveRepository {

    Optional<GiftCertificate> findById(long id, Boolean isActive);
    Optional<GiftCertificate> findByName(String name, Boolean isActive);
}
