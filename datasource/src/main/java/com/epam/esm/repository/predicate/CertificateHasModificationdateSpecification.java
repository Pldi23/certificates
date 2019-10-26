package com.epam.esm.repository.predicate;

import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.entity.criteria.ModificationDateCriteria;
import com.epam.esm.exception.CriteriaSearchTypeException;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static com.epam.esm.repository.constant.JpaConstant.MODIFICATION_DATE;

/**
 * to specify {@link GiftCertificate} by modification date
 *
 * @author Dzmitry Platonov on 2019-10-25.
 * @version 0.0.1
 */
public class CertificateHasModificationdateSpecification implements Specification<GiftCertificate> {

    private ModificationDateCriteria modificationDateCriteria;

    public CertificateHasModificationdateSpecification(ModificationDateCriteria modificationDateCriteria) {
        this.modificationDateCriteria = modificationDateCriteria;
    }

    @Override
    public List<Predicate> toPredicates(Root<GiftCertificate> root, CriteriaQuery query, CriteriaBuilder cb) {
        List<Predicate> predicates = new ArrayList<>();
        switch (modificationDateCriteria.getParameterSearchType()) {
            case IN:
                for (LocalDate date : modificationDateCriteria.getCriteriaList()) {
                    predicates.add(cb.equal(root.get(MODIFICATION_DATE), date));
                }
                break;
            case NOT_IN:
                for (LocalDate date : modificationDateCriteria.getCriteriaList()) {
                    predicates.add(cb.notEqual(root.get(MODIFICATION_DATE), date));
                }
                break;
            case BETWEEN:
                predicates.add(cb.between(root.get(MODIFICATION_DATE),
                        modificationDateCriteria.getCriteriaList().get(0),
                        modificationDateCriteria.getCriteriaList().get(1)));
                break;
            case NOT_BETWEEN:
                predicates.add(cb.lessThan(root.get(MODIFICATION_DATE),
                        modificationDateCriteria.getCriteriaList().get(0)));
                predicates.add(cb.greaterThan(root.get(MODIFICATION_DATE),
                        modificationDateCriteria.getCriteriaList().get(1)));
                break;
            default:
                throw new CriteriaSearchTypeException();
        }
        return predicates;
    }
}
