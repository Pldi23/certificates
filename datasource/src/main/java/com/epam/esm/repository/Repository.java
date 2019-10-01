package com.epam.esm.repository;

import com.epam.esm.specification.SqlSpecification;

import java.util.List;
import java.util.Optional;

public interface Repository<T> {

    Optional<T> add(T entity);
    void remove(T entity);
    void update(T entity);

    List<T> query(SqlSpecification specification);
}
