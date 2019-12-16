package com.epam.esm;


import com.epam.esm.repository.CertificateRepository;
import com.epam.esm.service.DataProcessingResult;
import com.epam.esm.service.PoolExecutionResult;
import com.epam.esm.service.FileService;

import java.io.IOException;

public class Application {

    public static void main(String[] args) throws IOException, InterruptedException {
        long start = System.currentTimeMillis();
        FileService fileService = new FileService(new CertificateRepository());
        PoolExecutionResult generateData = fileService.generateData();
        System.out.println(generateData.getValidFilesCount());
        System.out.println(generateData.getInvalidFilesCount());
        System.out.println(generateData.getExpectedCertificatesCount());
        DataProcessingResult dataProcessingResult = fileService.listenToFinishProcessing();
        System.out.println(dataProcessingResult.getInvalidFilesCount());
        System.out.println(dataProcessingResult.getValidCertificatesCount());
        System.out.println((System.currentTimeMillis() - start)/1000);
        fileService.destroy();
    }

}
