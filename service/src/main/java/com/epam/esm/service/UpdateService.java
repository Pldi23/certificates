package com.epam.esm.service;


import java.util.Optional;

public interface UpdateService<T> {

    Optional<T> update(T entity, long id);
}