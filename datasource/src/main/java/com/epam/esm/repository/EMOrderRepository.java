package com.epam.esm.repository;

import com.epam.esm.entity.Order;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static com.epam.esm.repository.constant.JpaConstant.*;

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
    public List<Order> findAll(String sortParam, int page, int size) {
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

    @Override
    public List<Order> findByCriteria(String sort, int page, int size, String email, Long userId,
                                      List<String> certificateNames, List<Long> certificateIds) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();

        CriteriaQuery<Order> criteriaQuery = criteriaBuilder.createQuery(Order.class);
        Root<Order> from = criteriaQuery.from(Order.class);
        CriteriaQuery<Order> select = criteriaQuery.select(from);
        if (email != null) {
            select.where(criteriaBuilder.like(from.get(USER).get(EMAIL), email));
        }
        if (userId != null) {
            select.where(criteriaBuilder.equal(from.get(USER).get(ID), userId));
        }
        if (certificateNames != null && !certificateNames.isEmpty()) {
            certificateNames.forEach(s -> select.where(
                    criteriaBuilder.equal(
                            from.join(ORDER_CERTIFICATE).join(CERTIFICATE).get(NAME), s)));
        }
        if (certificateIds != null && !certificateIds.isEmpty()) {
            certificateIds.forEach(i -> select.where(
                    criteriaBuilder.equal(
                            from.join(ORDER_CERTIFICATE).join(CERTIFICATE).get(ID), i)));
        }
        if (sort != null && sort.contains(PRICE)) {
            select.groupBy(from.get(ID));
            select.orderBy(sort.startsWith("-") ?
                    criteriaBuilder.asc(
                            criteriaBuilder.sum(
                                    from.join(ORDER_CERTIFICATE).get(buildSortParameter(sort.replaceFirst("-", ""))))) :
                    criteriaBuilder.desc(criteriaBuilder.sum(from.join(ORDER_CERTIFICATE).get(buildSortParameter(sort)))));
        }
        if (sort != null && sort.contains(ID)) {
            select.orderBy(sort.startsWith("-") ?
                    criteriaBuilder.asc(from.get(ID)) : criteriaBuilder.desc(from.get(ID)));
        }

        TypedQuery<Order> typedQuery = entityManager.createQuery(select);
        typedQuery.setFirstResult(page * size - size);
        typedQuery.setMaxResults(size);
        return typedQuery.getResultList();
    }

    @Override
    public BigDecimal calculatePrice(Long id) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();

        CriteriaQuery<BigDecimal> sumQuery = criteriaBuilder.createQuery(BigDecimal.class);
        Root<Order> from = sumQuery.from(Order.class);
        sumQuery.select(criteriaBuilder.sum(from.join(ORDER_CERTIFICATE).get(FIXED_PRICE)))
                .where(criteriaBuilder.equal(from.get(ID), id));
        return entityManager.createQuery(sumQuery).getSingleResult();
    }

    private String buildSortParameter(String sortParameter) {
        return sortParameter.replaceFirst(PRICE, FIXED_PRICE);
    }
}