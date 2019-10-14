package com.epam.esm.repository.hibernate;

import com.epam.esm.entity.User;

/**
 * gift-certificates
 *
 * @author Dzmitry Platonov on 2019-10-14.
 * @version 0.0.1
 */
public interface AbstractUserRepository extends SaveRepository<User>, RemoveRepository, FindOneRepository<User>,
        FindAllRepository<User> {

}
