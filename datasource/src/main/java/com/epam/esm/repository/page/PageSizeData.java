package com.epam.esm.repository.page;

import lombok.extern.log4j.Log4j2;

import javax.persistence.Query;

/**
 * implementation of {@link Pageable} for all entities
 *
 * @author Dzmitry Platonov on 2019-10-24.
 * @version 0.0.1
 */
@Log4j2
public class PageSizeData implements Pageable {

    private int page;
    private int size;

    public PageSizeData(int page, int size) {
        this.page = page;
        this.size = size;
    }

    @Override
    public Query setPageAndSize(Query query) {
        log.info(size);
        log.info(page * size - size);
        int firstResult = page * size - size > 0 ? page * size - size : 0;
        return query.setMaxResults(size).setFirstResult(firstResult);
    }
}
