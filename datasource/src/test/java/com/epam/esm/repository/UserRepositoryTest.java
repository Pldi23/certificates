package com.epam.esm.repository;

//import com.epam.esm.DatabaseSetupExtension;
import com.epam.esm.TestConfig;
import com.epam.esm.entity.Role;
import com.epam.esm.entity.User;
import com.epam.esm.repository.jpa.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.*;

/**
 * gift-certificates
 *
 * @author Dzmitry Platonov on 2019-10-10.
 * @version 0.0.1
 */
@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = TestConfig.class)
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    @Sql(statements = {"delete from application_user",
            "insert into application_role (value) values ('admin'), ('user'), ('guest');",
            "INSERT INTO application_user(email, password, role_id) VALUES ('pldi@mail.ru', 'Qwertyui1!', 3);"})
    void testDb() {
        assertEquals(1, userRepository.count());
    }

    @Test
    @DisplayName("should add user to repository and return")
    void testAdd() {
        User user =User.builder()
                .id(1L)
                .email("pldi@gmail.com")
                .password("Qwertyui1!")
                .role(Role.builder().id(1L).value("admin").build())
                .build();

        User actual = userRepository.save(user);

        assertEquals(user, actual);
    }

    @Test
    @DisplayName("should delete user and return ")
    void testDelete() {

    }

}