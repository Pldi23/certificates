package com.epam.esm.repository.hibernate;

import com.epam.esm.entity.Order;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;
import java.util.Optional;

/**
 * gift-certificates
 *
 * @author Dzmitry Platonov on 2019-10-14.
 * @version 0.0.1
 */
@Repository
public class EMOrderRepository implements AbstractOrderRepository {

    private EntityManager entityManager;

    public EMOrderRepository(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public List<Order> findAll() {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Order> query = criteriaBuilder.createQuery(Order.class);
        Root<Order> root = query.from(Order.class);
        query.select(root);
        return entityManager.createQuery(query).getResultList();
    }

    @Override
    public Optional<Order> findById(long id) {
        return Optional.ofNullable(entityManager.find(Order.class, id));
    }

    @Override
    public void deleteById(long id) {
        Optional<Order> optional = Optional.ofNullable(entityManager.find(Order.class, id));
        if (optional.isPresent()) {
            entityManager.remove(optional.get());
        } else throw new EmptyResultDataAccessException(0);
    }

    @Override
    public Order save(Order order) {
        if (order.getId() == null) {
            entityManager.persist(order);
            return order;
        } else {
            return entityManager.merge(order);
        }
    }
}
