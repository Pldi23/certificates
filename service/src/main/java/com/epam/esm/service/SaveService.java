package com.epam.esm.service;

import java.util.Optional;

public interface SaveService<T> {

    T save(T entity);
}
