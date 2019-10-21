package com.epam.esm.repository;

import java.util.Optional;


public interface SaveRepository<T> {

    T save(T entity);
}
