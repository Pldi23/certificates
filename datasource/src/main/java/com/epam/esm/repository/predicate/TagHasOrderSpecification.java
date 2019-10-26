package com.epam.esm.repository.predicate;

import com.epam.esm.entity.Order;
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
 * to specify {@link Tag} by order
 *
 * @author Dzmitry Platonov on 2019-10-25.
 * @version 0.0.1
 */
public class TagHasOrderSpecification implements Specification<Tag> {

    private Long id;

    public TagHasOrderSpecification(Long id) {
        this.id = id;
    }

    @Override
    public List<Predicate> toPredicates(Root<Tag> root, CriteriaQuery query, CriteriaBuilder cb) {
        Subquery<Order> orderSubquery = query.subquery(Order.class);
        Root<Order> orderRoot = orderSubquery.from(Order.class);
        Expression<Collection<Tag>> orderTags = orderRoot.join(JpaConstant.ORDER_CERTIFICATE).join(JpaConstant.CERTIFICATE).get(JpaConstant.TAGS);
        orderSubquery.select(orderRoot);
        orderSubquery.where(cb.equal(orderRoot.get(JpaConstant.ID), id), cb.isMember(root, orderTags));
        return List.of(cb.exists(orderSubquery));
    }
}
