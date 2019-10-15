package com.epam.esm.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * gift-certificates
 *
 * @author Dzmitry Platonov on 2019-10-14.
 * @version 0.0.1
 */
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PageAndSortDTO {

    private String sortParameter;
    private int page;
    private int size;


}
