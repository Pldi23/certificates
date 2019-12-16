package com.epam.esm.service;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

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

    public PoolExecutionResult(List<ThreadExecutionResult> executionResults, long currentDatabaseRows, long currentErrorFiles) {
        this.validFilesCount = executionResults.stream().mapToInt(ThreadExecutionResult::getValidFilesCount).sum();
        this.invalidFilesCount = executionResults.stream().mapToInt(ThreadExecutionResult::getInvalidFilesCount).sum() + currentErrorFiles;
        this.expectedCertificatesCount = executionResults.stream().mapToInt(ThreadExecutionResult::getValidCertificatesCount).sum() + currentDatabaseRows;
    }
}
