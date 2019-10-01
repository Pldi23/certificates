package com.epam.esm.repository;

import com.epam.esm.specification.SqlSpecification;

import java.util.List;

public interface Repository<T> {

    void add(T entity);
    void remove(T entity);
    void update(T entity);

    List<T> query(SqlSpecification specification);
}
