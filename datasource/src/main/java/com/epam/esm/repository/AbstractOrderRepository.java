package com.epam.esm.repository;

import com.epam.esm.entity.Order;
import com.epam.esm.repository.predicate.Specification;

import java.util.List;


public interface AbstractOrderRepository extends SaveRepository<Order>, RemoveRepository, FindOneRepository<Order>,
        FindAllSpecifiedRepository<Order> {

    long countLastPage(List<Specification<Order>> specifications, int size);
}
