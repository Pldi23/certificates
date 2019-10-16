package com.epam.esm.service;

import com.epam.esm.dto.PageAndSortDTO;

import java.util.List;


public interface FindAllService<T> {

    List<T> findAll(PageAndSortDTO pageAndSortDTO);
}
