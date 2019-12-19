package com.epam.esm.listener;

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
@NoArgsConstructor
@AllArgsConstructor
public class DataProcessingResult {

    private long invalidFilesCount;
    private long validCertificatesCount;

}
