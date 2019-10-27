package com.epam.esm.repository;

import com.epam.esm.entity.OrderCertificate;

import java.math.BigDecimal;
import java.util.List;


public interface AbstractOrderCertificateRepository extends SaveRepository<OrderCertificate>, SaveBatchRepository<OrderCertificate> {

    List<OrderCertificate> findAll();
    List<OrderCertificate> findAllByOrderId(Long orderId);
    void deleteByOrderId(Long orderId);
    BigDecimal calculateOrderFixedPrice(Long orderId);


}
