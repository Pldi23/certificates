package com.epam.esm.service;

import com.epam.esm.dto.BrokenGiftCertificateDTO;
import com.epam.esm.dto.GiftCertificateDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.Callable;

/**
 * utility
 *
 * @author Dzmitry Platonov on 2019-12-12.
 * @version 0.0.1
 */
@Slf4j
public class FilesCreator implements Callable<ThreadExecutionResult> {

    private static final int CERTIFICATES_LIST_SIZE = 3;
    private static final String CERTIFICATE_KEY_NAME = "/cerificates-";
    private static final String VIOLATION_CERTIFICATE_NAME = "violates db constraints name";
    private static final String CERTIFICATE_DESCRIPTION = "description";
    private static final String CERTIFICATE_BROKEN_NAME = "broken name";
    private static final int COUNTER_DIVIDER = 20;
    private static final BigDecimal INVALID_CERTIFICATE_PRICE = new BigDecimal(-1);
    private static final BigDecimal VALID_CERTIFICATE_PRICE = new BigDecimal(1);


    private Path path;
    private long finish;

    public FilesCreator(Path path, long finish) {
        this.path = path;
        this.finish = finish;
    }

    @Override
    public ThreadExecutionResult call() throws IOException {
        ThreadExecutionResult executionResult = new ThreadExecutionResult();
        int counter = 0;
        ObjectMapper mapper = new ObjectMapper();
        while (System.currentTimeMillis() < finish) {
            File file = new File(path.toString() + CERTIFICATE_KEY_NAME + counter);
            if (counter % COUNTER_DIVIDER != 9) {
                if (counter % COUNTER_DIVIDER != 3) {
                    List<GiftCertificateDTO> giftCertificateDTOS = createCertificateList(counter);
                    mapper.writeValue(file, giftCertificateDTOS);
                } else {
                    Files.createFile(file.toPath());
                }
            } else {
                List<BrokenGiftCertificateDTO> giftCertificateDTOS = createBrokenCertificatesList();
                mapper.writeValue(file, giftCertificateDTOS);
            }
            incrementExecutionResult(counter++, executionResult);
            file.setExecutable(true);
        }
        log.info(path + " : " + counter);
        return executionResult;
    }

    private void incrementExecutionResult(int counter, ThreadExecutionResult executionResult) {
        int divideResult = counter % COUNTER_DIVIDER;
        if (divideResult != 3 && divideResult != 9 && divideResult != 11 && divideResult != 12) {
            executionResult.incrementValidFilesCount();
            executionResult.incrementValidCertificatesCount(CERTIFICATES_LIST_SIZE);
        } else {
            executionResult.incrementInValidFilesCount();
        }
    }

    private List<GiftCertificateDTO> createCertificateList(int counter) {
        List<GiftCertificateDTO> giftCertificateDTOS = new ArrayList<>(CERTIFICATES_LIST_SIZE);
        for (int i = 0; i < CERTIFICATES_LIST_SIZE; i++) {
            GiftCertificateDTO giftCertificateDTO = GiftCertificateDTO.builder()
                    .name(counter % COUNTER_DIVIDER == 12 ? VIOLATION_CERTIFICATE_NAME : UUID.randomUUID().toString())
                    .description(CERTIFICATE_DESCRIPTION)
                    .price(counter % COUNTER_DIVIDER == 11 ? INVALID_CERTIFICATE_PRICE : VALID_CERTIFICATE_PRICE)
                    .expirationDate(LocalDate.now())
                    .tags(new HashSet())
                    .build();
            giftCertificateDTOS.add(giftCertificateDTO);
        }
        return giftCertificateDTOS;
    }

    private List<BrokenGiftCertificateDTO> createBrokenCertificatesList() {
        List<BrokenGiftCertificateDTO> giftCertificateDTOS = new ArrayList<>(CERTIFICATES_LIST_SIZE);
        for (int i = 0; i < CERTIFICATES_LIST_SIZE; i++) {
            BrokenGiftCertificateDTO giftCertificateDTO = BrokenGiftCertificateDTO.builder()
                    .brokenName(CERTIFICATE_BROKEN_NAME)
                    .brokenDescription(CERTIFICATE_DESCRIPTION)
                    .brokenPrice(null)
                    .brokenExpirationDate(null)
                    .brokenTags(null)
                    .build();
            giftCertificateDTOS.add(giftCertificateDTO);
        }
        return giftCertificateDTOS;
    }
}
