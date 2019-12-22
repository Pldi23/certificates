package com.epam.esm.process;

import lombok.extern.log4j.Log4j2;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedTransferQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Stream;


@Log4j2
public class FilesProducer implements Runnable {

    private ExecutorService consumerService;
    private List<FilesConsumer> consumers;
    private LinkedTransferQueue<Path> queue;
    private TaskProperties taskProperties;
    private AtomicBoolean isScanning;
    private AtomicInteger consumersCount;

    FilesProducer(ExecutorService consumerService, List<FilesConsumer> consumers,
                  LinkedTransferQueue<Path> queue, TaskProperties taskProperties,
                  AtomicBoolean isScanning, AtomicInteger consumersCount) {
        this.consumerService = consumerService;
        this.consumers = consumers;
        this.queue = queue;
        this.taskProperties = taskProperties;
        this.isScanning = isScanning;
        this.consumersCount = consumersCount;
    }

    @Override
    public void run() {
        Path path = Path.of(taskProperties.getFolder());
        isScanning.set(false);
        if (path.toFile().exists() && path.toFile().isDirectory()) {
            shouldStart(path);
            if (isScanning.get()) {
                consumers.forEach(consumer -> {
                    consumerService.execute(consumer);
                    consumersCount.incrementAndGet();
                });
                while (containsFiles(path)) {
                    transferPaths(path);
                    awaitConsumers(consumersCount);
                }
                isScanning.set(false);
                writeHook();
                consumersCount.set(0);
                log.info("hook written");
            }
        }
    }

    private void transferPaths(Path path) {
        try (Stream<Path> walk = Files.walk(path)) {
            walk.filter(p -> p.toFile().isFile() && p.toFile().canExecute()).forEach(transfer -> {
                try {
                    queue.transfer(transfer);
                } catch (InterruptedException e) {
                    log.warn("Interrupted", e);
                    Thread.currentThread().interrupt();
                }
            });
        } catch (IOException e) {
            log.warn("could not process file ", e);
        }
    }

    private void writeHook() {
        try {
            Files.createFile(Paths.get(taskProperties.getFolder(), taskProperties.getMarkerFileName()));
        } catch (IOException e) {
            log.warn("could not create hook file ", e);
        }
    }

    private void awaitConsumers(AtomicInteger consumersAmount) {
        while (queue.getWaitingConsumerCount() != consumersAmount.get()) {
            try {
                TimeUnit.MILLISECONDS.sleep(1);
            } catch (InterruptedException e) {
                log.warn("Interrupted", e);
                Thread.currentThread().interrupt();
            }
        }
    }

    private boolean containsFiles(Path path) {
        try (Stream<Path> stream = Files.walk(path)) {
            return stream.anyMatch(p -> Files.isRegularFile(p));
        } catch (IOException e) {
            log.warn("could not detect if files exists", e);
            return false;
        }
    }

    private void shouldStart(Path path) {
        try {
            Files.walkFileTree(path, new CheckRootFileVisitor(isScanning, taskProperties));
        } catch (IOException e) {
            isScanning.set(false);
        }
    }
}
