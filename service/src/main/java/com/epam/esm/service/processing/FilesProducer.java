package com.epam.esm.service.processing;

import lombok.extern.log4j.Log4j2;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Comparator;
import java.util.concurrent.LinkedTransferQueue;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Stream;

@Log4j2
public class FilesProducer implements Runnable {

    private Path path;
    private LinkedTransferQueue<Path> queue;
    private AtomicBoolean isScanning;

    public FilesProducer(Path path, LinkedTransferQueue<Path> synchronousQueue, AtomicBoolean isScanning) {
        this.path = path;
        this.queue = synchronousQueue;
        this.isScanning = isScanning;
    }

    @Override
    public void run() {
        log.info("producer started");
        while (path.toFile().exists() && path.toFile().isDirectory() && containsFiles(path)) {
//            while (true) {
//            isScanning.set(true);
            try (Stream<Path> pathStream = Files.walk(path)) {
                pathStream
                        .sorted(Comparator.reverseOrder())
                        .filter(pathToFilter -> pathToFilter.toFile().isFile())
                        .forEach(pathToFile -> {
                            try {
                                log.info(pathToFile + " producing");
                                queue.transfer(pathToFile);
                            } catch (InterruptedException e) {
                                log.warn("interrupted thread caught", e);
                                Thread.currentThread().interrupt();
                            }
                        });
            }
            catch (IOException e) {
                log.warn("IO caught during producing", e);
            }

        }
//        }
        log.info("producer finished");
        isScanning.set(false);
    }

    private boolean containsFiles(Path path) {
        try (Stream<Path> stream = Files.walk(path)) {
            return stream.anyMatch(p -> !Files.isDirectory(p));
        } catch (IOException e) {
            return false;
        }
    }
}
