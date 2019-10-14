package com.epam.esm.repository.hibernate;

import java.util.Optional;

public interface FindOneRepository<T> {

    Optional<T> findById(long id);
}
