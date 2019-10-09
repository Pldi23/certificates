package com.epam.esm.repository;

import com.epam.esm.entity.GiftCertificate;

/**
 * giftcertificates
 *
 * @author Dzmitry Platonov on 2019-10-03.
 * @version 0.0.1
 */
public interface AbstractCertificateRepository extends FindAllRepository<GiftCertificate>,
        FindOneRepository<GiftCertificate>, QueryRepository<GiftCertificate>, SaveRepository<GiftCertificate>,
        RemoveRepository, UpdateRepository<GiftCertificate> {
}