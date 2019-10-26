package com.epam.esm.repository.predicate;

import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.entity.criteria.PriceCriteria;
import com.epam.esm.exception.CriteriaSearchTypeException;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static com.epam.esm.repository.constant.JpaConstant.PRICE;

/**
 * to specify {@link GiftCertificate} by price
 *
 * @author Dzmitry Platonov on 2019-10-25.
 * @version 0.0.1
 */
public class CertificateHasPriceSpecification implements Specification<GiftCertificate> {

    private PriceCriteria priceCriteria;

    public CertificateHasPriceSpecification(PriceCriteria priceCriteria) {
        this.priceCriteria = priceCriteria;
    }

    @Override
    public List<Predicate> toPredicates(Root<GiftCertificate> root, CriteriaQuery query, CriteriaBuilder cb) {
        List<Predicate> predicates = new ArrayList<>();
        switch (priceCriteria.getParameterSearchType()) {
            case IN:
                for (BigDecimal price : priceCriteria.getCriteriaList()) {
                    predicates.add(cb.equal(root.get(PRICE), price));
                }
                break;
            case NOT_IN:
                for (BigDecimal price : priceCriteria.getCriteriaList()) {
                    predicates.add(cb.notEqual(root.get(PRICE), price));
                }
                break;
            case BETWEEN:
                predicates.add(cb.between(root.get(PRICE),
                        priceCriteria.getCriteriaList().get(0),
                        priceCriteria.getCriteriaList().get(1)));
                break;
            case NOT_BETWEEN:
                predicates.add(cb.lessThan(root.get(PRICE),
                        priceCriteria.getCriteriaList().get(0)));
                predicates.add(cb.greaterThan(root.get(PRICE),
                        priceCriteria.getCriteriaList().get(1)));
                break;
            default:
                throw new CriteriaSearchTypeException();
        }
        return predicates;
    }
}
