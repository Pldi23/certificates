package com.epam.esm.repository;

import com.epam.esm.repository.page.Pageable;
import com.epam.esm.repository.predicate.Specification;
import com.epam.esm.repository.sort.Sortable;

import java.util.List;


public interface FindAllSpecifiedRepository<T> {

    List<T> findAllSpecified(List<Specification<T>> specifications, Sortable<T> sortable, Pageable pageable);
}
