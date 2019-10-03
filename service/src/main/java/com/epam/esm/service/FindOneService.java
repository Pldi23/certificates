package com.epam.esm.service;

import java.util.Optional;


public interface FindOneService<T> {

    Optional<T> findOne(long id);
}
