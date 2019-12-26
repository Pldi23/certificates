package com.epam.esm.repository;

import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.repository.predicate.Specification;

import java.util.List;
import java.util.Optional;


public interface AbstractCertificateRepository extends SaveRepository<GiftCertificate>, FindAllSpecifiedRepository<GiftCertificate>,
        RemoveRepository {

    Optional<GiftCertificate> findById(long id, Boolean isActive);
    Optional<GiftCertificate> findByName(String name, Boolean isActive);
    long countLastPage(List<Specification<GiftCertificate>> specifications, int size);
    boolean saveMany(List<GiftCertificate> giftCertificates);
    boolean existsByNames(List<GiftCertificate> giftCertificates);
}
