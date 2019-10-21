package com.epam.esm.repository;

import java.util.Optional;

public interface FindOneRepository<T> {

    Optional<T> findById(long id);
}
