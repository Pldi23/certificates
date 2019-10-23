package com.epam.esm.repository;

import com.epam.esm.entity.Order;

import java.util.List;


public interface AbstractOrderRepository extends SaveRepository<Order>, RemoveRepository, FindOneRepository<Order>,
        FindAllRepository<Order> {

    List<Order> findByCriteria(String sort, int page, int size, String email, Long userId,
                               List<String> certificateNames, List<Long> certificateIds);

}
