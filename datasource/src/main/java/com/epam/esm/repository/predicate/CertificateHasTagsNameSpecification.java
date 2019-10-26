package com.epam.esm.repository.predicate;

import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.entity.Tag;
import com.epam.esm.entity.criteria.TagNameCriteria;
import com.epam.esm.exception.CriteriaSearchTypeException;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;

import static com.epam.esm.repository.constant.JpaConstant.TAGS;
import static com.epam.esm.repository.constant.JpaConstant.TITLE;

/**
 * to specify {@link GiftCertificate} by tag's name
 *
 * @author Dzmitry Platonov on 2019-10-25.
 * @version 0.0.1
 */
public class CertificateHasTagsNameSpecification implements Specification<GiftCertificate> {

    private TagNameCriteria tagNameCriteria;

    public CertificateHasTagsNameSpecification(TagNameCriteria tagNameCriteria) {
        this.tagNameCriteria = tagNameCriteria;
    }

    @Override
    public List<Predicate> toPredicates(Root<GiftCertificate> root, CriteriaQuery query, CriteriaBuilder cb) {
        List<Predicate> predicates = new ArrayList<>();
        switch (tagNameCriteria.getTextSearchType()) {
            case IN:
                for (String tagName : tagNameCriteria.getTagNames()) {
                    Join<GiftCertificate, Tag> join = root.join(TAGS);
                    predicates.add(cb.equal(join.get(TITLE), tagName));
                }
                break;
            case NOT_IN:
                for (String tagName : tagNameCriteria.getTagNames()) {
                    Join<GiftCertificate, Tag> join = root.join(TAGS);
                    predicates.add(cb.notEqual(join.get(TITLE), tagName));
                }
                break;
            case LIKE:
                for (String tagName : tagNameCriteria.getTagNames()) {
                    Join<GiftCertificate, Tag> join = root.join(TAGS);
                    predicates.add(cb.like(join.get(TITLE), "%" + tagName + "%"));
                }
                break;
            case NOT_LIKE:
                for (String tagName : tagNameCriteria.getTagNames()) {
                    Join<GiftCertificate, Tag> join = root.join(TAGS);
                    predicates.add(cb.notLike(join.get(TITLE), "%" + tagName + "%"));
                }
                break;
            default:
                throw new CriteriaSearchTypeException();
        }
        return predicates;
    }
}
