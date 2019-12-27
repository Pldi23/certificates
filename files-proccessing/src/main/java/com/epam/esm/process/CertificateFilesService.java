package com.epam.esm.process;

import com.epam.esm.repository.AbstractCertificateRepository;
import com.epam.esm.validate.Validator;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.FileVisitor;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedTransferQueue;
import java.util.concurrent.TimeUnit;
import java.util.stream.Stream;

@Log4j2
@Service
public class CertificateFilesService {

    private final AbstractCertificateRepository certificateRepository;
    private final LinkedTransferQueue<Path> queue;
    private final ExecutorService consumerService;
    private final TaskProperties taskProperties;
    private final Validator validator;
    private final ObjectMapper mapper;
    private final SystemMonitor systemMonitor;
    private final FileVisitor<Path> fileVisitor;

    public CertificateFilesService(AbstractCertificateRepository certificateRepository,
                                   TaskProperties taskProperties,
                                   Validator validator, SystemMonitor systemMonitor, FileVisitor<Path> fileVisitor) {
        this.queue = new LinkedTransferQueue<>();
        this.certificateRepository = certificateRepository;
        this.consumerService = Executors.newFixedThreadPool(taskProperties.getConsumerCount());
        this.taskProperties = taskProperties;
        this.validator = validator;
        this.mapper = new ObjectMapper();
        this.systemMonitor = systemMonitor;
        this.fileVisitor = fileVisitor;
    }

    /**
     * One thread contains scanner and producer functions to increase performance by saving number of threads.
     * Also there is no room for situations where application needs more than one producer,
     * if we wanted to increase the number of producers, we would have to lock every transfer of each file  or use additional queue/list,
     * which would drastically reduce the processing speed, because of losing profit of LinkedTransferQueue in my opinion.
     * Also we manage consumers pool from here to start it only after detecting files in root folder.
     */
    public void scanAndProduceIfNeeded() {
        mapper.registerModule(new JavaTimeModule());
        Path path = Path.of(taskProperties.getFolder());

        if (path.toFile().exists() && path.toFile().isDirectory()) {
            monitorSystemStatus(path);
            if (systemMonitor.getStatus()) {
                startConsumers();
                while (containsFiles(path)) {
                    transferPaths(path);
                    awaitConsumers();
                }
                systemMonitor.deActivateProducer();
                writeHookToUtility();
                stopConsumers();
                log.info("producing stopped, hook written");
            }
        }

    }

    private void startConsumers() {
        for (int i = 0; i < taskProperties.getConsumerCount(); i++) {
            consumerService.submit(new FilesConsumer(queue, certificateRepository, systemMonitor, taskProperties, validator, mapper));
            systemMonitor.incrementActiveConsumersCounter();
        }
    }

    private void stopConsumers() {
        for (int i = 0; i < systemMonitor.getActiveConsumersCount(); i++) {
            try {
                queue.transfer(Paths.get(taskProperties.getPoisonPillMarker()));
            } catch (InterruptedException e) {
                log.warn("interrupted", e);
                Thread.currentThread().interrupt();
            }

        }
        systemMonitor.resetConsumersCount();
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

    private void writeHookToUtility() {
        try {
            Files.createFile(Paths.get(taskProperties.getFolder(), taskProperties.getMarkerFileName()));
        } catch (IOException e) {
            log.warn("could not create hook file ", e);
        }
    }

    private void awaitConsumers() {
        while (queue.getWaitingConsumerCount() != systemMonitor.getActiveConsumersCount()) {
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
            return stream.anyMatch(p -> p.toFile().isFile() && !p.toString().contains(".DS_Store"));
        } catch (IOException e) {
            log.warn("could not detect if files exists", e);
            return false;
        }
    }

    private void monitorSystemStatus(Path path) {
        try {
            Files.walkFileTree(path, fileVisitor);
        } catch (IOException e) {
            systemMonitor.deActivateProducer();
        }
    }
}
