package com.epam.esm.service.processing;

import com.epam.esm.repository.JpaCertificateRepository;
import com.epam.esm.repository.JpaTagRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import javax.annotation.PreDestroy;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedTransferQueue;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Stream;

@Log4j2
@Service
public class CertificateFilesService {

    private JpaCertificateRepository certificateRepository;
    private JpaTagRepository tagRepository;
    private LinkedTransferQueue<Path> queue;
    private AtomicBoolean isScanning;
    private ScheduledExecutorService executorService;
    private ExecutorService consumerService;
    private TaskProperties taskProperties;

    public CertificateFilesService(JpaCertificateRepository certificateRepository,
                                   JpaTagRepository tagRepository,
                                   TaskProperties taskProperties) {
        this.certificateRepository = certificateRepository;
        this.tagRepository = tagRepository;
        this.queue = new LinkedTransferQueue<>();
        this.isScanning = new AtomicBoolean();
        this.executorService = Executors.newSingleThreadScheduledExecutor();
        this.consumerService = Executors.newScheduledThreadPool(taskProperties.getThreadCount());
        this.taskProperties = taskProperties;
    }

    public void listenToCertificatesData() {
        executorService.scheduleAtFixedRate(producer, taskProperties.getInitialDelay(), taskProperties.getScanDelay(), TimeUnit.MILLISECONDS);
    }

    private Runnable producer = () -> {
        log.info("scanning for files");
        Path path = Path.of(taskProperties.getFolder());
        isScanning.set(false);
        if (path.toFile().exists() && path.toFile().isDirectory()) {
            shouldStart(path);
            if (isScanning.get()) {

//            isScanning.set(true);
                int consumersCount = taskProperties.getThreadCount();
                log.info(consumersCount + "consumers starting");
//            CyclicBarrier barrier = new CyclicBarrier(consumersCount + 1);
//            AtomicBoolean shouldCheck = new AtomicBoolean(false);
                for (int i = 0; i < consumersCount; i++) {
                    consumerService.execute(new FilesConsumer(queue, certificateRepository, tagRepository,
                            isScanning, taskProperties
//                        barrier, shouldCheck
                    ));
                }
//            log.info("start to produce");
//            try {
                while (containsFiles(path)) {
//                    shouldCheck.set(false);
//                    log.info("while true");
                    transferPaths(path);
                    awaitConsumers(consumersCount);
//                    shouldCheck.set(true);
//                    awaitAtBarrier(barrier);
//                    TimeUnit.MILLISECONDS.sleep(100);
                }
                isScanning.set(false);
//                awaitAtBarrier(barrier);
                writeMarker();
                log.info("producer finished");

//            }
//            catch (InterruptedException e) {
//                log.warn("interrupted thread caught", e);
//                Thread.currentThread().interrupt();
//            }
            }
        }
    };


    private void transferPaths(Path path) {
//        log.info("transferring paths");

        try (Stream<Path> walk = Files.walk(path)) {
            walk.filter(p -> p.toFile().isFile() && p.toFile().canExecute()).forEach(transfer -> {
                try {

//                    log.info(transfer + " producing");
                    queue.transfer(transfer);
//                    log.info("to next transfer : " + transfer);
                } catch (InterruptedException e) {
                    log.warn("Interrupted", e);
                    Thread.currentThread().interrupt();
                }
            });
//            log.info("finish to transfer paths");
        } catch (IOException e) {
            log.warn("could not process file ", e);
        }
        log.info(queue.getWaitingConsumerCount());
    }

    private void writeMarker() {
        try {
            Files.createFile(Paths.get(taskProperties.getFolder(), taskProperties.getMarkerFileName()));
        } catch (IOException e) {
            log.warn("could create marker file ", e);
        }
    }

    private void awaitConsumers(int consumersAmount) {
        while (queue.getWaitingConsumerCount() != consumersAmount) {
            log.info("waiting for " + (consumersAmount - queue.getWaitingConsumerCount()) + " consumers");
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
            return stream.peek(file -> log.info("file : " + file)).anyMatch(p -> Files.isRegularFile(p) && p.toFile().canExecute() && !path.toString().contains("DS_Store"));
        } catch (IOException e) {
            log.warn("could not detect if files exists", e);
            return false;
        }
    }

    private void shouldStart(Path path) {
        try {
            Files.walkFileTree(path, new CheckRootFileVisitor(isScanning));
        } catch (IOException e) {
            isScanning.set(false);
        }
    }

    @PreDestroy
    public void destroy() {
        executorService.shutdown();
        consumerService.shutdown();
    }
}
