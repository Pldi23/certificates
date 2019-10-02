package com.epam.esm.repository;

import com.epam.esm.specification.SqlSpecification;

import java.util.List;
import java.util.Optional;

public interface Repository<T> {

    Optional<T> add(T entity);
    void remove(long id);
    boolean update(T entity);
    Optional<T> findOne(long id);

    List<T> query(SqlSpecification<T> specification);
}
