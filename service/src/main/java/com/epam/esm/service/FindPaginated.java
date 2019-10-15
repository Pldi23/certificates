package com.epam.esm.service;

import com.epam.esm.dto.PageAndSortDTO;

import java.util.List;

/**
 * gift-certificates
 *
 * @author Dzmitry Platonov on 2019-10-14.
 * @version 0.0.1
 */
public interface FindPaginated<T> {

    List<T> findPaginated(PageAndSortDTO pageAndSortDTO);
}
