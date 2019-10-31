package com.epam.esm.dto;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

/**
 * gift-certificates
 *
 * @author Dzmitry Platonov on 2019-10-30.
 * @version 0.0.1
 */
@Data
@Builder
public class TagDetailsDTO {

    private TagDTO tag;
    private BigDecimal cost;
    private long numOrders;


}
