package com.epam.esm.process;

import com.epam.esm.properties.UtilityConfiguration;
import com.epam.esm.service.DataStatistic;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.UUID;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;


@Slf4j
public class FilesCreator implements Runnable {

    private static final int BARRIER_TIMEOUT = 10;

    private Path path;
    private CyclicBarrier barrier;
    private DataStatistic dataStatistic;
    private UtilityConfiguration utilityConfiguration;

    FilesCreator(Path path, CyclicBarrier barrier, DataStatistic dataStatistic) {
        this.path = path;
        this.barrier = barrier;
        this.dataStatistic = dataStatistic;
        this.utilityConfiguration = UtilityConfiguration.getInstance();
    }

    @Override
    public void run() {
        int counter = 0;
        try {
            while (counter < utilityConfiguration.getFilesCount()) {
                Path filePath = writeJson(counter, this.path);
                counter++;
                filePath.toFile().setExecutable(true);
            }
            log.info(path + " : " + counter);
            barrier.await(BARRIER_TIMEOUT, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            log.warn("Interrupted", e);
            Thread.currentThread().interrupt();
        } catch (BrokenBarrierException e) {
            log.warn("Barrier broken", e);
        } catch (TimeoutException e) {
            log.warn("Time out", e);
        } catch (IOException e) {
            log.warn("could not write file ", e);
        }
    }


    private Path writeJson(int counter, Path path) throws IOException {
        return Files.write(Path.of(path.toString() + "/" + System.currentTimeMillis() + counter),
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
