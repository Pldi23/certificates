package com.epam.esm.repository.sort;


import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Order;
import javax.persistence.criteria.Root;

/**
 * to set sort parameters to {@link CriteriaQuery}
 *
 * @author Dzmitry Platonov on 2019-10-24.
 * @version 0.0.1
 */
public interface Sortable<T> {

    Order setOrder(Root<T> root, CriteriaQuery<T> query, CriteriaBuilder cb);
}
