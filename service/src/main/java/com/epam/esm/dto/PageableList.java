package com.epam.esm.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;

/**
 * gift-certificates
 *
 * @author Dzmitry Platonov on 2019-11-01.
 * @version 0.0.1
 */
@Data
@Builder
public class PageableList<T> {

    private List<T> list;
    private long lastPage;
}
