package com.epam.esm.service;

import lombok.Data;

import java.util.concurrent.atomic.AtomicInteger;


@Data
public class DataStatistic {

    private AtomicInteger validFilesCount;
    private AtomicInteger invalidFilesCount;

    DataStatistic() {
        this.validFilesCount = new AtomicInteger(0);
        this.invalidFilesCount = new AtomicInteger(0);
    }
}
