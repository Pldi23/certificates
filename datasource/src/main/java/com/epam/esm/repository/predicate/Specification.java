package com.epam.esm.repository.predicate;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.List;


public interface Specification<T> {

    List<Predicate> toPredicates(Root<T> root, CriteriaQuery query, CriteriaBuilder cb);
}
