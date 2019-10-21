package com.epam.esm.service;


public interface UpdateService<T> {

    T update(T entity, long id);
}
