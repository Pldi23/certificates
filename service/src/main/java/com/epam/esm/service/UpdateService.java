package com.epam.esm.service;


public interface UpdateService<T> {

    boolean update(T entity, long id);
}
