package com.epam.esm.repository;

import com.epam.esm.entity.Order;
import com.epam.esm.repository.page.Pageable;
import com.epam.esm.repository.predicate.Specification;
import com.epam.esm.repository.sort.Sortable;
import lombok.extern.log4j.Log4j2;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.epam.esm.repository.constant.JpaConstant.ID;
@Log4j2
@Repository
public class OrderRepository implements AbstractOrderRepository {

    private EntityManager entityManager;

    public OrderRepository(EntityManager entityManager) {
        this.entityManager = entityManager;
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
    public List<Order> findAllSpecified(List<Specification<Order>> specifications, Sortable<Order> sortable, Pageable pageable) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Order> query = criteriaBuilder.createQuery(Order.class);
        Root<Order> root = query.from(Order.class);

        query.groupBy(root.get(ID));
        if (sortable != null) {
            query.orderBy(sortable.setOrder(root, query, criteriaBuilder));
        }
        if (specifications != null) {
            List<Predicate> predicates = new ArrayList<>();
            for (Specification<Order> s : specifications) {
                predicates.addAll(s.toPredicates(root, query, criteriaBuilder));
            }
            query.where(criteriaBuilder.and(predicates.toArray(new Predicate[predicates.size()])));
        }
        return pageable.setPageAndSize(entityManager.createQuery(query)).getResultList();
    }

    @Override
    public long countLastPage(List<Specification<Order>> specifications, int size) {
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Long> cQuery = builder.createQuery(Long.class);
        Root<Order> from = cQuery.from(Order.class);
        CriteriaQuery<Long> select = cQuery.select(builder.count(from));
        select.groupBy(from.get(ID));
        if (specifications != null) {
            List<Predicate> predicates = new ArrayList<>();
            for (Specification<Order> s : specifications) {
                predicates.addAll(s.toPredicates(from, cQuery, builder));
            }
            select.where(builder.and(predicates.toArray(new Predicate[predicates.size()])));
        }
        TypedQuery<Long> typedQuery = entityManager.createQuery(select);
        Long numOfOrders = typedQuery.getSingleResult();
        long res = numOfOrders % size == 0 ? numOfOrders / size : numOfOrders / size + 1;
        log.info(res);
        return res;
    }
}
