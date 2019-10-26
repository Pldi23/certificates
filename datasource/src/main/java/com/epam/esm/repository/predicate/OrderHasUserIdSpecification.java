package com.epam.esm.repository.predicate;

import com.epam.esm.entity.Order;
import com.epam.esm.repository.constant.JpaConstant;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.List;

/**
 * to specify {@link Order} by user's id
 *
 * @author Dzmitry Platonov on 2019-10-24.
 * @version 0.0.1
 */
public class OrderHasUserIdSpecification implements Specification<Order> {

    private Long userId;

    public OrderHasUserIdSpecification(Long userId) {
        this.userId = userId;
    }

    @Override
    public List<Predicate> toPredicates(Root<Order> root, CriteriaQuery query, CriteriaBuilder cb) {
        return List.of(cb.equal(root.get(JpaConstant.USER).get(JpaConstant.ID), userId));
    }
}
