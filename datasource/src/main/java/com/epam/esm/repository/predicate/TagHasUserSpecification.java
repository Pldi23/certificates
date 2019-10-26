package com.epam.esm.repository.predicate;

import com.epam.esm.entity.Tag;
import com.epam.esm.entity.User;
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
 * to specify {@link Tag} by user
 *
 * @author Dzmitry Platonov on 2019-10-25.
 * @version 0.0.1
 */
public class TagHasUserSpecification implements Specification<Tag> {

    private Long id;

    public TagHasUserSpecification(Long id) {
        this.id = id;
    }

    @Override
    public List<Predicate> toPredicates(Root<Tag> root, CriteriaQuery query, CriteriaBuilder cb) {
        Subquery<User> subquery = query.subquery(User.class);
        Root<User> userRoot = subquery.from(User.class);
        Expression<Collection<Tag>> userTags = userRoot.join(JpaConstant.ORDERS)
                .join(JpaConstant.ORDER_CERTIFICATE)
                .join(JpaConstant.CERTIFICATE)
                .get(JpaConstant.TAGS);
        subquery.select(userRoot);
        subquery.where(cb.equal(userRoot.get(JpaConstant.ID), id), cb.isMember(root, userTags));
        return List.of(cb.exists(subquery));
    }
}
