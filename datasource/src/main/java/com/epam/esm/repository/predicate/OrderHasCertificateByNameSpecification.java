package com.epam.esm.repository.predicate;

import com.epam.esm.entity.Order;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.List;
import java.util.stream.Collectors;

import static com.epam.esm.repository.constant.JpaConstant.CERTIFICATE;
import static com.epam.esm.repository.constant.JpaConstant.ID;
import static com.epam.esm.repository.constant.JpaConstant.NAME;
import static com.epam.esm.repository.constant.JpaConstant.ORDER_CERTIFICATE;

/**
 * to specify {@link Order} by certificate's name
 *
 * @author Dzmitry Platonov on 2019-10-24.
 * @version 0.0.1
 */
public class OrderHasCertificateByNameSpecification implements Specification<Order> {

    private List<String> names;

    public OrderHasCertificateByNameSpecification(List<String> names) {
        this.names = names;
    }

    @Override
    public List<Predicate> toPredicates(Root<Order> root, CriteriaQuery query, CriteriaBuilder cb) {
        return names.stream()
                .map(i -> cb.equal(root.join(ORDER_CERTIFICATE).join(CERTIFICATE).get(NAME), i))
                .collect(Collectors.toList());
    }
}
