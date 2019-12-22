package com.epam.esm.process;

import com.epam.esm.properties.UtilityConfiguration;
import com.epam.esm.service.DataStatistic;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.UUID;
import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicInteger;

@Getter
@Slf4j
public class FilesCreator implements Runnable {

    private Path path;
    private DataStatistic dataStatistic;
    private UtilityConfiguration utilityConfiguration;
    private Semaphore semaphore;
    private AtomicInteger totalCreatorsCounter;

    FilesCreator(Path path,
                 DataStatistic dataStatistic,
                 Semaphore semaphore,
                 AtomicInteger totalCreatorsCounter) {
        this.path = path;
        this.dataStatistic = dataStatistic;
        this.utilityConfiguration = UtilityConfiguration.getInstance();
        this.semaphore = semaphore;
        this.totalCreatorsCounter = totalCreatorsCounter;
    }

    @Override
    public void run() {
        try {
            semaphore.acquire();
        } catch (InterruptedException e) {
            log.warn("Interrupted", e);
            Thread.currentThread().interrupt();
        }
        int counter = 0;
        try {
            while (counter < utilityConfiguration.getFilesCount()) {
                writeJson(counter++, this.path);
            }
            log.info(path + " : " + counter);
        } catch (IOException e) {
            log.warn("could not write file ", e);
        } finally {
            semaphore.release();
            totalCreatorsCounter.decrementAndGet();
        }
    }


    private void writeJson(int counter, Path path) throws IOException {
        Files.write(Path.of(path.toString() + "/" + System.currentTimeMillis() + counter),
                getJson(counter).getBytes(), StandardOpenOption.CREATE);
    }

    private String getJson(int counter) {
        int key = counter % utilityConfiguration.getGeneratorDivider();
        if (key == utilityConfiguration.getBrokenFieldIndex()) {
            dataStatistic.getInvalidFilesCount().incrementAndGet();
            return generateBrokenJson();
        }
        if (key == utilityConfiguration.getViolatesDbConstraintIndex()) {
            dataStatistic.getInvalidFilesCount().incrementAndGet();
            return generateConstraintViolationJson();
        }
        if (key == utilityConfiguration.getNonValidBeanIndex()) {
            dataStatistic.getInvalidFilesCount().incrementAndGet();
            return generateValidatorViolationJson();
        }
        if (key == utilityConfiguration.getWrongJsonFormatIndex()) {
            dataStatistic.getInvalidFilesCount().incrementAndGet();
            return "";
        }

        dataStatistic.getValidFilesCount().incrementAndGet();
        return generateValidJson();
    }

    private String generateValidJson() {
        return
                "[{\"id\":null,\"name\":\"" +
                        UUID.randomUUID().toString() +
                        "\",\"description\":\"description\",\"price\":1,\"creationDate\":null,\"modificationDate\":null," +
                        "\"expirationDate\":\"18/12/2020\",\"activeStatus\":null,\"tags\":[{\"title\": \"a\"},{\"title\": \"b\"},{\"title\": \"c\"}]}," +
                        "{\"id\":null,\"name\":\"" +
                        UUID.randomUUID().toString() +
                        "\",\"description\":\"description\",\"price\":1,\"creationDate\":null,\"modificationDate\":null," +
                        "\"expirationDate\":\"18/12/2020\",\"activeStatus\":null,\"tags\":[{\"title\": \"a\"},{\"title\": \"b\"}]}," +
                        "{\"id\":null,\"name\":\"" +
                        UUID.randomUUID().toString() +
                        "\",\"description\":\"description\",\"price\":1,\"creationDate\":null,\"modificationDate\":null," +
                        "\"expirationDate\":\"18/12/2020\",\"activeStatus\":null,\"tags\":[{\"title\": \"a\"}]}]";
    }

    private String generateBrokenJson() {
        return
                "[{\"brokenid\":null,\"brokenname\":\"name\",\"description\":\"description\",\"price\":1,\"creationDate\"" +
                        ":null,\"modificationDate\":null,\"expirationDate\":\"18/12/2020\",\"activeStatus\":null,\"tags\"" +
                        ":[]}," +
                        "{\"brokenid\":null,\"brokenname\":\"name\",\"description\":\"description\",\"price\":1," +
                        "\"creationDate\":null,\"modificationDate\":null,\"expirationDate\":\"18/12/2020\",\"activeStatus" +
                        "\":null,\"tags\":[]}," +
                        "{\"brokenid\":null,\"brokenname\":\"name\",\"description\":\"description\",\"price\":1," +
                        "\"creationDate\":null,\"modificationDate\":null,\"expirationDate\":\"18/12/2020\",\"activeStatus" +
                        "\":null,\"tags\":[]}]";

    }

    private String generateConstraintViolationJson() {
        return
                "[{\"id\":null,\"name\":\"violates db constraints name\",\"description\":\"description\",\"price\":1," +
                        "\"creationDate\":null,\"modificationDate\":null,\"expirationDate\":\"18/12/2020\",\"activeStatus" +
                        "\":null,\"tags\":[]}," +
                        "{\"id\":null,\"name\":\"violates db constraints name\",\"description\":\"description\"," +
                        "\"price\":1,\"creationDate\":null,\"modificationDate\":null,\"expirationDate\":\"18/12/2020\"," +
                        "\"activeStatus\":null,\"tags\":[]}," +
                        "{\"id\":null,\"name\":\"violates db constraints name\",\"description\":\"description\"," +
                        "\"price\":1,\"creationDate\":null,\"modificationDate\":null,\"expirationDate\":\"18/12/2020\"," +
                        "\"activeStatus\":null,\"tags\":[]}]";

    }

    private String generateValidatorViolationJson() {
        return
                "[{\"id\":null,\"name\":\"" + UUID.randomUUID().toString() + "\",\"description\":\"description\"," +
                        "\"price\":-1,\"creationDate\":null,\"modificationDate\":null,\"expirationDate\":\"18/12/2020\"," +
                        "\"activeStatus\":null,\"tags\":[]}," +
                        "{\"id\":null,\"name\":\"" + UUID.randomUUID().toString() + "\",\"description\":\"description\"," +
                        "\"price\":-1,\"creationDate\":null,\"modificationDate\":null,\"expirationDate\":\"18/12/2020\"," +
                        "\"activeStatus\":null,\"tags\":[]}," +
                        "{\"id\":null,\"name\":\"" + UUID.randomUUID().toString() + "\",\"description\":\"description\"," +
                        "\"price\":-1,\"creationDate\":null,\"modificationDate\":null,\"expirationDate\":\"18/12/2020\"," +
                        "\"activeStatus\":null,\"tags\":[]}]";
    }
}
