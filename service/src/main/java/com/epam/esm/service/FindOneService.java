package com.epam.esm.service;

import java.util.Optional;


public interface FindOneService<T> {

    T findOne(long id);
}
