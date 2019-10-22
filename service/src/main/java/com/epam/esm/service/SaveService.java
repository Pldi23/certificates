package com.epam.esm.service;

public interface SaveService<T> {

    T save(T entity);
}
