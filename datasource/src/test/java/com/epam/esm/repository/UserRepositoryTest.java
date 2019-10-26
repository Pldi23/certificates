package com.epam.esm.repository;

import com.epam.esm.TestConfig;
import com.epam.esm.entity.Role;
import com.epam.esm.entity.User;
import com.epam.esm.repository.page.PageSizeData;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
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
@DirtiesContext(classMode = AFTER_EACH_TEST_METHOD)
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

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
        assertEquals(Optional.empty(), userRepository.findById(1));
    }

    @Test
    @DisplayName("should find all users")
    @Sql(statements = {"insert into application_role (value) values ('admin'), ('user'), ('guest');" +
            "INSERT INTO application_user(email, password, role_id) VALUES ('pldi@gmail.com', 'Qwertyui1!', 1);"})
    void findAll() {
        User expected = User.builder()
                .id(1L)
                .email("pldi@gmail.com")
                .password("Qwertyui1!")
                .role(Role.builder().id(1L).value("admin").build())
                .orders(new HashSet<>())
                .build();

        assertEquals(List.of(expected), userRepository.findAllSpecified(null, null, new PageSizeData(1, 20)));
    }

    @Test
    @DisplayName("should find user with id 1")
    @Sql(statements = {"insert into application_role (value) values ('admin'), ('user'), ('guest');" +
            "INSERT INTO application_user(email, password, role_id) VALUES ('pldi@gmail.com', 'Qwertyui1!', 1);"})
    void findById() {
        Optional<User> expected = Optional.of(User.builder()
                .id(1L)
                .email("pldi@gmail.com")
                .password("Qwertyui1!")
                .role(Role.builder().id(1L).value("admin").build())
                .orders(new HashSet<>())
                .build());

        assertEquals(expected, userRepository.findById(1));
    }


    @Test
    @DisplayName("should find user with email pldi@dmail.com")
    @Sql(statements = {"insert into application_role (value) values ('admin'), ('user'), ('guest');" +
            "INSERT INTO application_user(email, password, role_id) VALUES ('pldi@gmail.com', 'Qwertyui1!', 1);"})
    void findByEmail() {
        Optional<User> expected = Optional.of(User.builder()
                .id(1L)
                .email("pldi@gmail.com")
                .password("Qwertyui1!")
                .role(Role.builder().id(1L).value("admin").build())
                .orders(new HashSet<>())
                .build());

        assertEquals(expected, userRepository.findByEmail("pldi@gmail.com"));
    }
}