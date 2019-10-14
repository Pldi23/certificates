package com.epam.esm.repository.jpa;

import com.epam.esm.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


public interface OrderRepository extends JpaRepository<Order, Long> {
}
