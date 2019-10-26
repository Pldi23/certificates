package com.epam.esm.repository.predicate;

import com.epam.esm.entity.GiftCertificate;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;

import static com.epam.esm.repository.constant.JpaConstant.ORDER;
import static com.epam.esm.repository.constant.JpaConstant.ORDER_CERTIFICATE;

/**
 * to specify {@link GiftCertificate} by order
 *
 * @author Dzmitry Platonov on 2019-10-25.
 * @version 0.0.1
 */
public class CertificateHasOrderIdSpecification implements Specification<GiftCertificate> {

    private Long orderId;

    public CertificateHasOrderIdSpecification(Long orderId) {
        this.orderId = orderId;
    }

    @Override
    public List<Predicate> toPredicates(Root<GiftCertificate> root, CriteriaQuery query, CriteriaBuilder cb) {
        List<Predicate> predicates = new ArrayList<>();
        predicates.add(cb.equal(root.join(ORDER_CERTIFICATE).get(ORDER), orderId));
        return predicates;
    }
}
