package com.epam.esm.repository.hibernate;

import java.util.List;

/**
 * giftcertificates
 *
 * @author Dzmitry Platonov on 2019-10-03.
 * @version 0.0.1
 */
public interface FindAllRepository<T> {

    List<T> findAll();
}
