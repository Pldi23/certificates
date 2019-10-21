package com.epam.esm.repository;

import com.epam.esm.entity.User;

import java.util.Optional;

/**
 * gift-certificates
 *
 * @author Dzmitry Platonov on 2019-10-14.
 * @version 0.0.1
 */
public interface AbstractUserRepository extends SaveRepository<User>, RemoveRepository, FindOneRepository<User>,
        FindAllRepository<User> {

    Optional<User> findByEmail(String email);
    Optional<User> findByEmailPassword(String email, String password);
    long count();
}
