package com.epam.esm.repository.page;

import javax.persistence.Query;

/**
 * implementation of {@link Pageable} for all entities
 *
 * @author Dzmitry Platonov on 2019-10-24.
 * @version 0.0.1
 */
public class PageSizeData implements Pageable {

    private int page;
    private int size;

    public PageSizeData(int page, int size) {
        this.page = page;
        this.size = size;
    }

    @Override
    public Query setPageAndSize(Query query) {
        return query.setMaxResults(size).setFirstResult(page * size - size);
    }
}
