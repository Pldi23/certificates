package com.epam.esm.repository;

import com.epam.esm.entity.OrderCertificate;

import java.math.BigDecimal;
import java.util.List;

/**
 * gift-certificates
 *
 * @author Dzmitry Platonov on 2019-10-20.
 * @version 0.0.1
 */
public interface AbstractOrderCertificateRepository extends SaveRepository<OrderCertificate> {

    List<OrderCertificate> findAllByCertificateId(Long certificateId);
    List<OrderCertificate> findAll();
    List<OrderCertificate> findAllByOrderId(Long orderId);
    void deleteByOrderId(Long orderId);
    BigDecimal calculateOrderFixedPrice(Long orderId);
}
