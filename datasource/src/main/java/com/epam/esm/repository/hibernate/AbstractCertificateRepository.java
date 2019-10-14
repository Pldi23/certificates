package com.epam.esm.repository.hibernate;

import com.epam.esm.entity.GiftCertificate;

import java.util.List;
import java.util.Optional;

/**
 * giftcertificates
 *
 * @author Dzmitry Platonov on 2019-10-03.
 * @version 0.0.1
 */
public interface AbstractCertificateRepository extends FindAllRepository<GiftCertificate>,
        FindOneRepository<GiftCertificate>, QueryRepository<GiftCertificate>, SaveRepository<GiftCertificate>,
        RemoveRepository {

    Optional<GiftCertificate> findByName(String name);
    List<GiftCertificate> findByTagsId(long tagId);
}
