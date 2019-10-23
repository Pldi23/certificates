package com.epam.esm.repository.jpa;

import com.epam.esm.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * gift-certificates
 *
 * @author Dzmitry Platonov on 2019-10-10.
 * @version 0.0.1
 */
//@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);
}
