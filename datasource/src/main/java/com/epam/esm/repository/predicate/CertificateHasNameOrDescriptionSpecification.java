package com.epam.esm.repository.predicate;

import com.epam.esm.entity.GiftCertificate;
import lombok.extern.log4j.Log4j2;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;

/**
 * gift-certificates
 *
 * @author Dzmitry Platonov on 2019-11-15.
 * @version 0.0.1
 */
@Log4j2
public class CertificateHasNameOrDescriptionSpecification implements Specification<GiftCertificate> {

    private CertificateHasNameSpecification certificateHasNameSpecification;
    private CertificateHasDescriptionSpecification certificateHasDescriptionSpecification;

    public CertificateHasNameOrDescriptionSpecification(CertificateHasNameSpecification certificateHasNameSpecification,
                                                        CertificateHasDescriptionSpecification certificateHasDescriptionSpecification) {
        this.certificateHasNameSpecification = certificateHasNameSpecification;
        this.certificateHasDescriptionSpecification = certificateHasDescriptionSpecification;
    }


    @Override
    public List<Predicate> toPredicates(Root<GiftCertificate> root, CriteriaQuery query, CriteriaBuilder cb) {
        List<Predicate> predicates = new ArrayList<>();
        predicates.add(cb.or(certificateHasNameSpecification.toPredicates(root, query, cb).get(0), certificateHasDescriptionSpecification.toPredicates(root, query, cb).get(0)));
        return predicates;
    }
}
