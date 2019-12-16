package com.epam.esm.service;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * utility
 *
 * @author Dzmitry Platonov on 2019-12-13.
 * @version 0.0.1
 */
@Data
@AllArgsConstructor
public class DataProcessingResult {

    private long invalidFilesCount;
    private long validCertificatesCount;

    public DataProcessingResult() {
    }
}
