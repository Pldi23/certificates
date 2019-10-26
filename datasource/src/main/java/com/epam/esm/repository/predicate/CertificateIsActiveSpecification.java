package com.epam.esm.repository.predicate;

import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.repository.constant.JpaConstant;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.List;

/**
 * to specify {@link GiftCertificate} by active status
 *
 * @author Dzmitry Platonov on 2019-10-26.
 * @version 0.0.1
 */
public class CertificateIsActiveSpecification implements Specification<GiftCertificate> {
    @Override
    public List<Predicate> toPredicates(Root<GiftCertificate> root, CriteriaQuery query, CriteriaBuilder cb) {
        return List.of(cb.equal(root.get(JpaConstant.ACTIVE_STATUS), true));
    }
}
