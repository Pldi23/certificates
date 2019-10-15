package com.epam.esm.repository.hibernate;

import com.epam.esm.entity.Order;

import java.math.BigDecimal;
import java.util.List;

/**
 * gift-certificates
 *
 * @author Dzmitry Platonov on 2019-10-14.
 * @version 0.0.1
 */
public interface AbstractOrderRepository extends SaveRepository<Order>, RemoveRepository, FindOneRepository<Order>,
        FindAllRepository<Order> {

    List<Order> findByCriteria(String sort, int page, int size, String email, Long userId,
                               List<String> certificateNames, List<Long> certificateIds);

    BigDecimal calculatePrice(Long id);
}
