package com.epam.esm.service;

import lombok.Data;


/**
 * utility
 *
 * @author Dzmitry Platonov on 2019-12-13.
 * @version 0.0.1
 */
@Data
public class ThreadExecutionResult {

    private int validFilesCount;
    private int invalidFilesCount;
    private int validCertificatesCount;

    public void incrementValidFilesCount() {
        validFilesCount++;
    }

    public void incrementInValidFilesCount() {
        invalidFilesCount++;
    }

    public void incrementValidCertificatesCount(int increaseValue) {
        validCertificatesCount += increaseValue;
    }

}
