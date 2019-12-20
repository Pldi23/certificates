package com.epam.esm.listener;

import com.epam.esm.exception.RepositoryException;
import com.epam.esm.properties.UtilityConfiguration;
import com.epam.esm.repository.Repository;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;
import java.util.stream.Stream;


@Slf4j
public class DataProcessingListener implements Callable<DataProcessingResult> {

    private static final int SCANNING_INTERVAL = 10; //milliseconds

    private Path rootPath;
    private Path errorsPath;
    private Repository repository;

    public DataProcessingListener(Path rootPath, Path errorsPath, Repository repository) {
        this.rootPath = rootPath;
        this.errorsPath = errorsPath;
        this.repository = repository;
    }

    @Override
    public DataProcessingResult call() throws InterruptedException {
        while (shouldScan(rootPath)) {
            TimeUnit.MILLISECONDS.sleep(SCANNING_INTERVAL);
        }
        long errorFilesCounter = countErrorFiles(errorsPath);
        try {
            long count = repository.count();
            return new DataProcessingResult(errorFilesCounter, count);
        } catch (RepositoryException e) {
            log.warn("could not calculate statistic", e);
            return new DataProcessingResult(errorFilesCounter, -1);
        }
    }

    private boolean shouldScan(Path rootPath) {
        try {
            return !Files.deleteIfExists(Paths.get(rootPath.toString(), UtilityConfiguration.getInstance().getMarkerFilename()));

        } catch (IOException e) {
            log.warn("scan could not be completed");
            return true;
        }
    }

    private long countErrorFiles(Path errorsPath) {
        if (Files.notExists(errorsPath)) {
            log.warn("error folder not found, could not calculate statistic");
            return -1;
        }
        try (Stream<Path> stream = Files.walk(errorsPath)) {
            return stream.filter(path -> !Files.isDirectory(path)).count();
        } catch (IOException e) {
            log.warn("could not count files correctly", e);
            return -1;
        }
    }

}
