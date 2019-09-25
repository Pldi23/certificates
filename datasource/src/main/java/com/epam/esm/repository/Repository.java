package com.epam.esm.repository;

import com.epam.esm.specification.SqlSpecification;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * gift certificates
 *
 * @author Dzmitry Platonov on 2019-09-23.
 * @version 0.0.1
 */
@Component
public interface Repository<T> {

    void add(T entity);
    void remove(T entity);
    void update(T entity);

    List<T> query(SqlSpecification specification);
}
