package com.epam.esm.repository.predicate;

import com.epam.esm.entity.Order;
import com.epam.esm.repository.constant.JpaConstant;
import lombok.ToString;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.List;

/**
 * to specify {@link Order} by user's email
 *
 * @author Dzmitry Platonov on 2019-10-24.
 * @version 0.0.1
 */
@ToString
public class OrderHasUserEmailSpecification implements Specification<Order> {

    private String email;

    public OrderHasUserEmailSpecification(String email) {
        this.email = email;
    }

    @Override
    public List<Predicate> toPredicates(Root<Order> root, CriteriaQuery query, CriteriaBuilder cb) {
        return List.of(cb.equal(root.get(JpaConstant.USER).get(JpaConstant.EMAIL), email));
    }
}
