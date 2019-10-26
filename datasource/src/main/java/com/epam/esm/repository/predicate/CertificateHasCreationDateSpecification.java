package com.epam.esm.repository.predicate;

import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.entity.criteria.CreationDateCriteria;
import com.epam.esm.exception.CriteriaSearchTypeException;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static com.epam.esm.repository.constant.JpaConstant.CREATION_DATE;

/**
 * to specify {@link GiftCertificate} by creation date
 *
 * @author Dzmitry Platonov on 2019-10-25.
 * @version 0.0.1
 */
public class CertificateHasCreationDateSpecification implements Specification<GiftCertificate> {

    private CreationDateCriteria creationDateCriteria;

    public CertificateHasCreationDateSpecification(CreationDateCriteria creationDateCriteria) {
        this.creationDateCriteria = creationDateCriteria;
    }


    @Override
    public List<Predicate> toPredicates(Root<GiftCertificate> root, CriteriaQuery query, CriteriaBuilder cb) {
        List<Predicate> predicates = new ArrayList<>();
        switch (creationDateCriteria.getParameterSearchType()) {
            case IN:
                for (LocalDate date : creationDateCriteria.getCriteriaList()) {
                    predicates.add(cb.equal(root.get(CREATION_DATE), date));
                }
                break;
            case NOT_IN:
                for (LocalDate date : creationDateCriteria.getCriteriaList()) {
                    predicates.add(cb.notEqual(root.get(CREATION_DATE), date));
                }
                break;
            case BETWEEN:
                predicates.add(cb.between(root.get(CREATION_DATE),
                        creationDateCriteria.getCriteriaList().get(0),
                        creationDateCriteria.getCriteriaList().get(1)));
                break;
            case NOT_BETWEEN:
                predicates.add(cb.lessThan(root.get(CREATION_DATE),
                        creationDateCriteria.getCriteriaList().get(0)));
                predicates.add(cb.greaterThan(root.get(CREATION_DATE),
                        creationDateCriteria.getCriteriaList().get(1)));
                break;
            default:
                throw new CriteriaSearchTypeException();
        }
        return predicates;
    }
}
