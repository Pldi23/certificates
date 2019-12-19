package com.epam.esm.service;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * utility
 *
 * @author Dzmitry Platonov on 2019-12-13.
 * @version 0.0.1
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PoolExecutionResult {

    private int validFilesCount;
    private long invalidFilesCount;
    private long expectedCertificatesCount;

    public PoolExecutionResult(DataStatistic dataStatistic, long currentDatabaseRows, long currentErrorFiles) {
        this.validFilesCount = dataStatistic.getValidFilesCount().get();
        this.invalidFilesCount = currentErrorFiles + dataStatistic.getInvalidFilesCount().get();
        this.expectedCertificatesCount = currentDatabaseRows + dataStatistic.getValidFilesCount().get() * 3;

    }
}
