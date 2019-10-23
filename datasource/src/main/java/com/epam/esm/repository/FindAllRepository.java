package com.epam.esm.repository;

import java.util.List;


public interface FindAllRepository<T> {

    List<T> findAll(String sortParam, int page, int size);
}
