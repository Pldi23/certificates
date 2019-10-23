package com.epam.esm.service;

public interface FindOneService<T> {

    T findOne(long id);
}
