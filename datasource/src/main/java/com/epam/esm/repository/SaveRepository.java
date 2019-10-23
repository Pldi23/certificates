package com.epam.esm.repository;

public interface SaveRepository<T> {

    T save(T entity);
}
