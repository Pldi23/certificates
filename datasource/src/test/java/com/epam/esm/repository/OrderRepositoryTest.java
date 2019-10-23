package com.epam.esm.repository;

import com.epam.esm.TestConfig;
import com.epam.esm.entity.Order;
import com.epam.esm.entity.Role;
import com.epam.esm.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.annotation.DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD;

@Transactional
@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = TestConfig.class)
@DirtiesContext(classMode = AFTER_EACH_TEST_METHOD)
class OrderRepositoryTest {

    @Autowired
    private OrderRepository orderRepository;

    private Order expected;


    @BeforeEach
    void setUp() {
        expected = Order.builder()
                .id(1L)
                .user(User.builder().id(1L)
                        .email("pldi@mail.ru")
                        .password("Qwertyui1!")
                        .role(Role.builder().id(1L).value("admin").build()).build())
                .createdAt(null)
                .activeStatus(true)
                .orderCertificates(new ArrayList<>())
                .build();
    }

    @Test
    @DisplayName("should find all orders")
    @Sql(statements = {"insert into application_role (value) values ('admin'), ('user'), ('guest');",
            "INSERT INTO application_user(email, password, role_id) VALUES ('pldi@mail.ru', 'Qwertyui1!', 1);",
            "insert into application_order (user_id, created_at, active_status) values (1, null, true)"})
    void findAll() {
        List<Order> actual = orderRepository.findAll("price", 1, Integer.MAX_VALUE);
        assertEquals(List.of(expected), actual);
    }

    @Test
    @DisplayName("should find order by id 1")
    @Sql(statements = {"insert into application_role (value) values ('admin'), ('user'), ('guest');",
            "INSERT INTO application_user(email, password, role_id) VALUES ('pldi@mail.ru', 'Qwertyui1!', 1);",
            "insert into application_order (user_id, created_at, active_status) values (1, null, true)"})
    void findById() {
        Optional<Order> actual = orderRepository.findById(1L);
        assertEquals(Optional.of(expected), actual);
    }

    @Test
    @DisplayName("should delete order by id (1)")
    @Sql(statements = {"insert into application_role (value) values ('admin'), ('user'), ('guest');",
            "INSERT INTO application_user(email, password, role_id) VALUES ('pldi@mail.ru', 'Qwertyui1!', 1);",
            "insert into application_order (user_id, created_at, active_status) values (1, null, true)"})
    void deleteById() {
        orderRepository.deleteById(1);
        assertEquals(Optional.empty(), orderRepository.findById(1));
    }

    @Test
    @DisplayName("should save order and return")
    @Sql(statements = {"insert into application_role (value) values ('admin'), ('user'), ('guest');",
            "INSERT INTO application_user(email, password, role_id) VALUES ('pldi@mail.ru', 'Qwertyui1!', 1);",
            "insert into application_order (user_id, created_at, active_status) values (1, null, true)"})
    void save() {
        Order actual = orderRepository.save(expected);
        assertEquals(expected, actual);
    }

    @Test
    @DisplayName("should find order 1 by criteria")
    @Sql(statements = {"insert into application_role (value) values ('admin'), ('user'), ('guest');",
            "INSERT INTO application_user(email, password, role_id) VALUES ('pldi@mail.ru', 'Qwertyui1!', 1);",
            "insert into application_order (user_id, created_at, active_status) values (1, null, true)"})
    void findByCriteria() {
        List<Order> actual = orderRepository.findByCriteria("id", 1, Integer.MAX_VALUE,
                null, null, null, null);

        assertEquals(List.of(expected), actual);
    }
}