package com.epam.esm.repository;

import com.epam.esm.TestConfig;
import com.epam.esm.entity.Order;
import com.epam.esm.entity.Role;
import com.epam.esm.entity.User;
import com.epam.esm.repository.page.PageSizeData;
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

import static org.junit.jupiter.api.Assertions.assertEquals;
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
    @Sql(statements = {"insert into application_role (value, id) values ('admin', 1), ('user', 2), ('guest', 3);",
            "INSERT INTO application_user(email, password, role_id, id) VALUES ('pldi@mail.ru', 'Qwertyui1!', 1, 1);",
            "insert into application_order (user_id, created_at, active_status, id) values (1, null, true, 1)"})
    void findAll() {
        List<Order> actual = orderRepository.findAllSpecified(null, null, new PageSizeData(1, 20));
        assertEquals(List.of(expected), actual);
    }

    @Test
    @DisplayName("should find order by id 1")
    @Sql(statements = {"insert into application_role (value, id) values ('admin', 1), ('user', 2), ('guest', 3);",
            "INSERT INTO application_user(email, password, role_id, id) VALUES ('pldi@mail.ru', 'Qwertyui1!', 1, 1);",
            "insert into application_order (user_id, created_at, active_status, id) values (1, null, true, 1)"})
    void findById() {
        Optional<Order> actual = orderRepository.findById(1L);
        assertEquals(Optional.of(expected), actual);
    }

    @Test
    @DisplayName("should delete order by id (1)")
    @Sql(statements = {"insert into application_role (value, id) values ('admin', 1), ('user', 2), ('guest', 3);",
            "INSERT INTO application_user(email, password, role_id, id) VALUES ('pldi@mail.ru', 'Qwertyui1!', 1, 1);",
            "insert into application_order (user_id, created_at, active_status, id) values (1, null, true, 1)"})
    void deleteById() {
        orderRepository.deleteById(1);
        assertEquals(Optional.empty(), orderRepository.findById(1));
    }

    @Test
    @DisplayName("should save order and return")
    @Sql(statements = {"insert into application_role (value, id) values ('admin', 1), ('user', 2), ('guest', 3);",
            "INSERT INTO application_user(email, password, role_id, id) VALUES ('pldi@mail.ru', 'Qwertyui1!', 1, 1);",
            "insert into application_order (user_id, created_at, active_status, id) values (1, null, true, 1)"})
    void save() {
        Order actual = orderRepository.save(expected);
        assertEquals(expected, actual);
    }

    @Test
    @DisplayName("should find order 1 by sort")
    @Sql(statements = {"insert into application_role (value, id) values ('admin', 1), ('user', 2), ('guest', 3);",
            "INSERT INTO application_user(email, password, role_id, id) VALUES ('pldi@mail.ru', 'Qwertyui1!', 1, 1);",
            "insert into application_order (user_id, created_at, active_status, id) values (1, null, true, 1)"})
    void findByCriteria() {
        List<Order> actual = orderRepository.findAllSpecified(null, null, new PageSizeData(1, 20));

        assertEquals(List.of(expected), actual);
    }
}