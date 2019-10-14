package com.epam.esm.repository.hibernate;

import java.util.Optional;


public interface SaveRepository<T> {

    T save(T entity);
}
