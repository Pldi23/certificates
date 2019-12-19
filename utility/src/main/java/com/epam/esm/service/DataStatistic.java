package com.epam.esm.service;

import lombok.Data;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * utility
 *
 * @author Dzmitry Platonov on 2019-12-18.
 * @version 0.0.1
 */
@Data
public class DataStatistic {

    private AtomicInteger validFilesCount;
    private AtomicInteger invalidFilesCount;

    public DataStatistic() {
        this.validFilesCount = new AtomicInteger(0);
        this.invalidFilesCount = new AtomicInteger(0);
    }
}
