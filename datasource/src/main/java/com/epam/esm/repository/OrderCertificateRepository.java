package com.epam.esm.repository;

import com.epam.esm.entity.OrderCertificate;
import com.epam.esm.repository.constant.QueryConstant;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.math.BigDecimal;
import java.util.List;

@Repository
public class OrderCertificateRepository implements AbstractOrderCertificateRepository {

    private EntityManager entityManager;

    public OrderCertificateRepository(EntityManager entityManager) {
        this.entityManager = entityManager;
    }


    @Override
    public OrderCertificate save(OrderCertificate orderCertificate) {
        entityManager.persist(orderCertificate);
        return orderCertificate;
    }

    @Override
    public List<OrderCertificate> findAll() {
        return entityManager.createNativeQuery(QueryConstant.SELECT_ALL_ORDER_CERTIFICATE, OrderCertificate.class).getResultList();
    }

    @Override
    public List<OrderCertificate> findAllByOrderId(Long orderId) {

        Query query = entityManager.createNativeQuery(QueryConstant.SELECT_ALL_ORDER_CERTIFICATE_BY_ORDER_ID, OrderCertificate.class);
        query.setParameter(1, orderId);
        return query.getResultList();
    }

    @Override
    public void deleteByOrderId(Long orderId) {
        Query query = entityManager.createNativeQuery(QueryConstant.DELETE_ORDER_CERTIFICATE_BY_ORDER_ID);
        query.setParameter(1, orderId);
        query.executeUpdate();
    }

    @Override
    public BigDecimal calculateOrderFixedPrice(Long orderId) {
        Query query = entityManager.createNativeQuery(QueryConstant.CALCULATE_ORDER_FIXED_PRICE);
        query.setParameter(1, orderId);
        return (BigDecimal) query.getSingleResult();
    }
}
