package com.epam.esm.repository.hibernate;


import java.util.Optional;

public interface UpdateRepository<T> {

    Optional<T> update(T entity);
}
