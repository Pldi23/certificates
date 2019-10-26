package com.epam.esm.repository.predicate;

import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.entity.Tag;
import com.epam.esm.entity.criteria.TagCriteria;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;

import static com.epam.esm.repository.constant.JpaConstant.ID;
import static com.epam.esm.repository.constant.JpaConstant.TAGS;

/**
 * to specify {@link GiftCertificate} by tag's id
 *
 * @author Dzmitry Platonov on 2019-10-25.
 * @version 0.0.1
 */
public class CertificateHasTagsIdSpecification implements Specification<GiftCertificate> {

    private TagCriteria tagCriteria;

    public CertificateHasTagsIdSpecification(TagCriteria tagCriteria) {
        this.tagCriteria = tagCriteria;
    }

    @Override
    public List<Predicate> toPredicates(Root<GiftCertificate> root, CriteriaQuery query, CriteriaBuilder cb) {
        List<Predicate> predicates = new ArrayList<>();
        for (long tagId : tagCriteria.getTagIds()) {
            Join<GiftCertificate, Tag> join = root.join(TAGS);
            predicates.add(cb.equal(join.get(ID), tagId));
        }
        return predicates;
    }
}
