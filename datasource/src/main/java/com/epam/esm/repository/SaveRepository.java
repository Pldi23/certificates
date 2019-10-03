package com.epam.esm.repository;

import java.util.Optional;


public interface SaveRepository<T> {

    Optional<T> save(T entity);
}
