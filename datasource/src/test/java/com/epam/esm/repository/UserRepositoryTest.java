package com.epam.esm.repository;

import com.epam.esm.TestConfig;
import com.epam.esm.entity.Role;
import com.epam.esm.entity.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.annotation.DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD;

/**
 * gift-certificates
 *
 * @author Dzmitry Platonov on 2019-10-10.
 * @version 0.0.1
 */
@Transactional
@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = TestConfig.class)
//@Sql(statements = "insert into application_role (value) values ('admin'), ('user'), ('guest');", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@DirtiesContext(classMode = AFTER_EACH_TEST_METHOD)
class UserRepositoryTest {

    @Autowired
    private EMUserRepository userRepository;

    @Test
    @Sql(statements = {"insert into application_role (value) values ('admin'), ('user'), ('guest');",
            "INSERT INTO application_user(email, password, role_id) VALUES ('pldi@mail.ru', 'Qwertyui1!', 3);"})
    void testDb() {
        assertEquals(1, userRepository.findAll(null, 1, Integer.MAX_VALUE).size());
    }

    @Test
    @DisplayName("should add user to repository and return")
    @Sql(statements = "insert into application_role (value) values ('admin'), ('user'), ('guest');")
    void testAdd() {
        User user = User.builder()
                .id(1L)
                .email("pldi@gmail.com")
                .password("Qwertyui1!")
                .role(Role.builder().id(1L).value("admin").build())
                .build();

        User actual = userRepository.save(user);

        assertEquals(user, actual);
    }

    @Test
    @DisplayName("should delete user")
    @Sql(statements = {"insert into application_role (value) values ('admin'), ('user'), ('guest');",
            "INSERT INTO application_user(email, password, role_id) VALUES ('pldi@mail.ru', 'Qwertyui1!', 3);"})
    void testDelete() {
        userRepository.deleteById(1);

    }

}