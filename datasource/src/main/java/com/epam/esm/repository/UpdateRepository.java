package com.epam.esm.repository;


public interface UpdateRepository<T> {

    boolean update(T entity);
}
