package com.epam.esm.repository.hibernate;

import com.epam.esm.entity.Order;

/**
 * gift-certificates
 *
 * @author Dzmitry Platonov on 2019-10-14.
 * @version 0.0.1
 */
public interface AbstractOrderRepository extends SaveRepository<Order>, RemoveRepository, FindOneRepository<Order>,
        FindAllRepository<Order> {
}
