package com.epam.esm.repository.predicate;

import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.entity.criteria.DescriptionCriteria;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;

import static com.epam.esm.repository.constant.JpaConstant.DESCRIPTION;

/**
 * to specify {@link GiftCertificate} by description
 *
 * @author Dzmitry Platonov on 2019-10-25.
 * @version 0.0.1
 */
public class CertificateHasDescriptionSpecification implements Specification<GiftCertificate> {

    private DescriptionCriteria descriptionCriteria;

    public CertificateHasDescriptionSpecification(DescriptionCriteria descriptionCriteria) {
        this.descriptionCriteria = descriptionCriteria;
    }

    @Override
    public List<Predicate> toPredicates(Root<GiftCertificate> root, CriteriaQuery query, CriteriaBuilder cb) {
        List<Predicate> predicates = new ArrayList<>();
        switch (descriptionCriteria.getSearchType()) {
            case IN:
                for (String name : descriptionCriteria.getCriteriaList()) {
                    predicates.add(cb.equal(root.get(DESCRIPTION), name));
                }
                break;
            case NOT_IN:
                for (String name : descriptionCriteria.getCriteriaList()) {
                    predicates.add(cb.notEqual(root.get(DESCRIPTION), name));
                }
                break;
            case LIKE:
                for (String name : descriptionCriteria.getCriteriaList()) {
                    predicates.add(cb.like(cb.lower(root.get(DESCRIPTION)), "%" + name.toLowerCase() + "%"));
                }
                break;
            case NOT_LIKE:
                for (String name : descriptionCriteria.getCriteriaList()) {
                    predicates.add(cb.notLike(cb.lower(root.get(DESCRIPTION)), "%" + name.toLowerCase() + "%"));
                }
                break;
        }
        return predicates;
    }
}
