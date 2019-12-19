package com.epam.esm.listener;

import com.epam.esm.properties.UtilityConfiguration;
import com.epam.esm.repository.Repository;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.FileVisitor;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;
import java.util.stream.Stream;

/**
 * utility
 *
 * @author Dzmitry Platonov on 2019-12-13.
 * @version 0.0.1
 */
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
        log.info("data processor listening");

        while (shouldScan(rootPath)) {
            TimeUnit.MILLISECONDS.sleep(SCANNING_INTERVAL);
        }
        long errorFilesCounter = countErrorFiles(errorsPath);
        return new DataProcessingResult(errorFilesCounter, repository.count());
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
        try (Stream<Path> stream = Files.walk(errorsPath)) {
            return stream.filter(path -> !Files.isDirectory(path)).count();
        } catch (IOException e) {
            log.warn("could not count files correctly", e);
            return -1;
        }
    }

}
