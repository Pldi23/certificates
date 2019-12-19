package com.epam.esm;


import com.epam.esm.repository.CertificateRepository;
import com.epam.esm.listener.DataProcessingResult;
import com.epam.esm.service.PoolExecutionResult;
import com.epam.esm.service.FileService;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Application {

    public static void main(String[] args) {
        long start = System.currentTimeMillis();
        FileService fileService = new FileService(new CertificateRepository());
        PoolExecutionResult generateData = fileService.generateData();
        log.info("valid files generated : " + generateData.getValidFilesCount());
        log.info("invalid files expected : " + generateData.getInvalidFilesCount());
        log.info("certificates in db expected : " + generateData.getExpectedCertificatesCount());
        log.info("generation time : " + (System.currentTimeMillis() - start)/1000);
        DataProcessingResult dataProcessingResult = fileService.listenToFinishProcessing();
        log.info("invalid files result : " + dataProcessingResult.getInvalidFilesCount());
        log.info("certificates count : " + dataProcessingResult.getValidCertificatesCount());
        log.info("time executed : " + (System.currentTimeMillis() - start)/1000);
        fileService.destroy();
    }

}
