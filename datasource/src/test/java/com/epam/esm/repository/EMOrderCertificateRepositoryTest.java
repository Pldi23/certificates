package com.epam.esm.repository;

import com.epam.esm.TestConfig;
import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.entity.Order;
import com.epam.esm.entity.OrderCertificate;
import com.epam.esm.entity.User;
import com.epam.esm.repository.jpa.OrderRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.annotation.DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD;

/**
 * gift-certificates
 *
 * @author Dzmitry Platonov on 2019-10-22.
 * @version 0.0.1
 */
@Transactional
@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = TestConfig.class)
@DirtiesContext(classMode = AFTER_EACH_TEST_METHOD)
class EMOrderCertificateRepositoryTest {

    @Autowired
    EMOrderCertificateRepository orderCertificateRepository;

    @Autowired
    EMCertificateRepository certificateRepository;

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
        orderCertificateRepository.save(og2);
    }
}