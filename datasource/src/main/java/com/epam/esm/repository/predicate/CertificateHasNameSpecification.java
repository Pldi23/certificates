package com.epam.esm.repository.predicate;

import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.entity.criteria.NameCriteria;
import com.epam.esm.exception.CriteriaSearchTypeException;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;

import static com.epam.esm.repository.constant.JpaConstant.NAME;

/**
 * to specify {@link GiftCertificate} by name
 *
 * @author Dzmitry Platonov on 2019-10-25.
 * @version 0.0.1
 */
public class CertificateHasNameSpecification implements Specification<GiftCertificate> {

    private NameCriteria nameCriteria;

    public CertificateHasNameSpecification(NameCriteria nameCriteria) {
        this.nameCriteria = nameCriteria;
    }

    @Override
    public List<Predicate> toPredicates(Root<GiftCertificate> root, CriteriaQuery query, CriteriaBuilder cb) {
        List<Predicate> predicates = new ArrayList<>();
        switch (nameCriteria.getSearchType()) {
            case IN:
                for (String name : nameCriteria.getCriteriaList()) {
                    predicates.add(cb.equal(root.get(NAME), name));
                }
                break;
            case NOT_IN:
                for (String name : nameCriteria.getCriteriaList()) {
                    predicates.add(cb.notEqual(root.get(NAME), name));
                }
                break;
            case LIKE:
                for (String name : nameCriteria.getCriteriaList()) {
                    predicates.add(cb.like(root.get(NAME), "%" + name + "%"));
                }
                break;
            case NOT_LIKE:
                for (String name : nameCriteria.getCriteriaList()) {
                    predicates.add(cb.notLike(root.get(NAME), "%" + name + "%"));
                }
                break;
            default:
                throw new CriteriaSearchTypeException();
        }
        return predicates;
    }
}
