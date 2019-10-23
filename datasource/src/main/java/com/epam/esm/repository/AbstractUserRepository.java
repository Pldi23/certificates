package com.epam.esm.repository;

import com.epam.esm.entity.User;

import java.util.Optional;


public interface AbstractUserRepository extends SaveRepository<User>, RemoveRepository, FindOneRepository<User>,
        FindAllRepository<User> {

    Optional<User> findByEmail(String email);
}
