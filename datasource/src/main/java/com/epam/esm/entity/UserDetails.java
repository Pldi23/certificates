package com.epam.esm.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * gift-certificates
 *
 * @author Dzmitry Platonov on 2019-10-14.
 * @version 0.0.1
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserDetails {

    private String mostUsedTags;
    private BigDecimal highestCost;
}
