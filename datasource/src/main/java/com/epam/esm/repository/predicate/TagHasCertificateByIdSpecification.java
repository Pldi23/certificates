package com.epam.esm.repository.predicate;

import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.entity.Tag;
import com.epam.esm.repository.constant.JpaConstant;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.Subquery;
import java.util.Collection;
import java.util.List;

/**
 * to specify {@link Tag} by certificate
 *
 * @author Dzmitry Platonov on 2019-10-25.
 * @version 0.0.1
 */
public class TagHasCertificateByIdSpecification implements Specification<Tag> {

    private Long id;

    public TagHasCertificateByIdSpecification(Long id) {
        this.id = id;
    }

    @Override
    public List<Predicate> toPredicates(Root<Tag> root, CriteriaQuery query, CriteriaBuilder cb) {
        Subquery<GiftCertificate> certificateSubquery = query.subquery(GiftCertificate.class);
        Root<GiftCertificate> certificateRoot = certificateSubquery.from(GiftCertificate.class);
        Expression<Collection<Tag>> certificateTags = certificateRoot.get(JpaConstant.TAGS);
        certificateSubquery.select(certificateRoot);
        certificateSubquery.where(cb.equal(certificateRoot.get(JpaConstant.ID), id), cb.isMember(root, certificateTags));
        return List.of(cb.exists(certificateSubquery));
    }
}
