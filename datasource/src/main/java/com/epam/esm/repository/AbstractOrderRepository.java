package com.epam.esm.repository;

import com.epam.esm.entity.Order;


public interface AbstractOrderRepository extends SaveRepository<Order>, RemoveRepository, FindOneRepository<Order>,
        FindAllSpecifiedRepository<Order> {

}
