package com.epam.esm.repository.predicate;

import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.entity.criteria.IdCriteria;
import com.epam.esm.exception.CriteriaSearchTypeException;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;

import static com.epam.esm.repository.constant.JpaConstant.ID;

/**
 * to specify {@link GiftCertificate} by id
 *
 * @author Dzmitry Platonov on 2019-10-25.
 * @version 0.0.1
 */
public class CertificateHasIdSpecification implements Specification<GiftCertificate> {

    private IdCriteria idCriteria;

    public CertificateHasIdSpecification(IdCriteria idCriteria) {
        this.idCriteria = idCriteria;
    }

    @Override
    public List<Predicate> toPredicates(Root<GiftCertificate> root, CriteriaQuery query, CriteriaBuilder cb) {
        List<Predicate> predicates = new ArrayList<>();
        switch (idCriteria.getParameterSearchType()) {
            case IN:
                for (long id : idCriteria.getCriteriaList()) {
                    predicates.add(cb.equal(root.get(ID), id));
                }
                break;
            case NOT_IN:
                for (long id : idCriteria.getCriteriaList()) {
                    predicates.add(cb.notEqual(root.get(ID), id));
                }
                break;
            case BETWEEN:
                predicates.add(cb.between(root.get(ID),
                        idCriteria.getCriteriaList().get(0),
                        idCriteria.getCriteriaList().get(1)));
                break;
            case NOT_BETWEEN:
                predicates.add(cb.lessThan(root.get(ID),
                        idCriteria.getCriteriaList().get(0)));
                predicates.add(cb.greaterThan(root.get(ID),
                        idCriteria.getCriteriaList().get(1)));

                break;
            default:
                throw new CriteriaSearchTypeException();
        }
        return predicates;
    }
}
