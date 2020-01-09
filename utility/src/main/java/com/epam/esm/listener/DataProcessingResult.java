package com.epam.esm.listener;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class DataProcessingResult {

    private long invalidFilesCount;
    private long validCertificatesCount;
    private long processingFinishTime;

}
