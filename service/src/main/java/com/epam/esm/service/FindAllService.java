package com.epam.esm.service;

import com.epam.esm.dto.PageAndSortDTO;
import com.epam.esm.dto.PageableList;


public interface FindAllService<T> {

    PageableList<T> findAll(PageAndSortDTO pageAndSortDTO);
}
