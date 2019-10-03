package com.epam.esm.service;

import java.util.Optional;

public interface SaveService<T> {

    Optional<T> save(T entity);
}
