package com.epam.esm.repository;

import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.entity.criteria.SearchCriteria;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

/**
 * giftcertificates
 *
 * @author Dzmitry Platonov on 2019-10-03.
 * @version 0.0.1
 */
public interface AbstractCertificateRepository extends QueryRepository<GiftCertificate>, SaveRepository<GiftCertificate>,
        RemoveRepository {

    List<GiftCertificate> findAll(String sortParam, int page, int size, boolean isActive);
    Optional<GiftCertificate> findById(long id, boolean isActive);
    Optional<GiftCertificate> findByName(String name);
    List<GiftCertificate> findByTagsId(long tagId, String sortParam, int page, int size);
    List<GiftCertificate> findByCriteria(SearchCriteria searchCriteria, String sortParameter, int page, int size);
    BigDecimal getPrice(Long id);
    List<GiftCertificate> findByOrder(Long orderId,  String sortParameter, int page, int size);
    long count();
}
