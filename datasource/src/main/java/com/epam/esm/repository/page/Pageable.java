package com.epam.esm.repository.page;

import javax.persistence.Query;

/**
 * to set limit and offset parameters to query
 *
 * @author Dzmitry Platonov on 2019-10-24.
 * @version 0.0.1
 */
public interface Pageable {

    Query setPageAndSize(Query query);
}
