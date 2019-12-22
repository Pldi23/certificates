package com.epam.esm.service;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class PoolExecutionResult {

    private static final int CERTIFICATES_PER_FILE = 3;

    private int validFilesCount;
    private long invalidFilesCount;
    private long expectedCertificatesCount;

    PoolExecutionResult(DataStatistic dataStatistic, long currentDatabaseRows, long currentErrorFiles) {
        this.validFilesCount = dataStatistic.getValidFilesCount().get();
        this.invalidFilesCount = currentErrorFiles != -1 ?
                currentErrorFiles + dataStatistic.getInvalidFilesCount().get() : -1;
        this.expectedCertificatesCount = currentDatabaseRows != -1 ?
                currentDatabaseRows + dataStatistic.getValidFilesCount().get() * CERTIFICATES_PER_FILE : -1;

    }
}
