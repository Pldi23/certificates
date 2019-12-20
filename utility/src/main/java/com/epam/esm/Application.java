package com.epam.esm;


import com.epam.esm.repository.CertificateRepository;
import com.epam.esm.listener.DataProcessingResult;
import com.epam.esm.service.PoolExecutionResult;
import com.epam.esm.service.FileService;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Application {

    private static final String CALCULATION_ERROR_MESSAGE = "could not be calculated";

    public static void main(String[] args) {
        long start = System.currentTimeMillis();
        FileService fileService = new FileService(new CertificateRepository());
        PoolExecutionResult generateData = fileService.generateData();
        log.info("valid files generated : " + generateData.getValidFilesCount());
        log.info("invalid files expected : " + getMessage(generateData.getInvalidFilesCount()));
        log.info("certificates in db expected : " + getMessage(generateData.getExpectedCertificatesCount()));
        log.info("generation time : " + (System.currentTimeMillis() - start)/1000 + " sec");
        DataProcessingResult dataProcessingResult = fileService.listenToFinishProcessing();
        log.info("invalid files result : " + getMessage(dataProcessingResult.getInvalidFilesCount()));
        log.info("certificates count : " + getMessage(dataProcessingResult.getValidCertificatesCount()));
        log.info("time executed : " + (System.currentTimeMillis() - start)/1000);
        fileService.destroy();
    }

    private static String getMessage(long result) {
        return result != -1 ? String.valueOf(result) : CALCULATION_ERROR_MESSAGE;
    }

}
