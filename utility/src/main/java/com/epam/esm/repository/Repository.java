package com.epam.esm.repository;

import com.epam.esm.exception.RepositoryException;


public interface Repository {

    long count() throws RepositoryException;
    boolean insertDbConstraintObject() throws RepositoryException;
}
