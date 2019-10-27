package com.epam.esm.repository;

import com.epam.esm.TestConfig;
import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.entity.Order;
import com.epam.esm.entity.OrderCertificate;
import com.epam.esm.entity.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.springframework.test.annotation.DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD;


@Transactional
@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = TestConfig.class)
@DirtiesContext(classMode = AFTER_EACH_TEST_METHOD)
class OrderCertificateRepositoryTest {

    @Autowired
    OrderCertificateRepository orderCertificateRepository;

    @Autowired
    CertificateRepository certificateRepository;

    @Autowired
    OrderRepository orderRepository;

    @Test
    @DisplayName("should not fail due to DataIntegrityViolationException")
    void save() {

        Order o = Order.builder().user(User.builder().email("pl@m.ru").build()).build();
        GiftCertificate g = GiftCertificate.builder().name("1").build();
        OrderCertificate og = OrderCertificate.builder().order(o).certificate(g).build();
        OrderCertificate og2 = OrderCertificate.builder().order(o).certificate(g).build();
        orderRepository.save(o);
        certificateRepository.save(g);
        orderCertificateRepository.save(og);
        OrderCertificate actual = orderCertificateRepository.save(og2);
        Assertions.assertEquals(og2, actual);
    }


    @Test
    @DisplayName("batch insert should insert and return list of OrdersCertificates")
    void saveBatch() {
        Order o = Order.builder().user(User.builder().email("pl@m.ru").build()).build();
        GiftCertificate g = GiftCertificate.builder().name("1").build();
        OrderCertificate og = OrderCertificate.builder().order(o).certificate(g).build();
        OrderCertificate og2 = OrderCertificate.builder().order(o).certificate(g).build();
        OrderCertificate og3 = OrderCertificate.builder().order(o).certificate(g).build();
        OrderCertificate og4 = OrderCertificate.builder().order(o).certificate(g).build();
        OrderCertificate og5 = OrderCertificate.builder().order(o).certificate(g).build();
        OrderCertificate og6 = OrderCertificate.builder().order(o).certificate(g).build();
        OrderCertificate og7 = OrderCertificate.builder().order(o).certificate(g).build();
        OrderCertificate og8 = OrderCertificate.builder().order(o).certificate(g).build();
        OrderCertificate og9 = OrderCertificate.builder().order(o).certificate(g).build();
        OrderCertificate og10 = OrderCertificate.builder().order(o).certificate(g).build();
        OrderCertificate og11 = OrderCertificate.builder().order(o).certificate(g).build();
        orderRepository.save(o);
        certificateRepository.save(g);
        List<OrderCertificate> actual = orderCertificateRepository.save(List.of(og, og2, og3, og4, og5, og6, og7, og8, og9, og10, og11));
        List<OrderCertificate> expected = List.of(og, og2, og3, og4, og5, og6, og7, og8, og9, og10, og11);
        Assertions.assertEquals(expected, actual);

    }

}