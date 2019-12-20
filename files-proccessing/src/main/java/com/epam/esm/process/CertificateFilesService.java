package com.epam.esm.process;

import com.epam.esm.repository.JpaCertificateRepository;
import com.epam.esm.repository.JpaTagRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import javax.annotation.PreDestroy;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedTransferQueue;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

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
        this.queue = new LinkedTransferQueue<>();
        this.certificateRepository = certificateRepository;
        this.tagRepository = tagRepository;
        this.isScanning = new AtomicBoolean();
        this.executorService = Executors.newSingleThreadScheduledExecutor();
        this.consumerService = Executors.newScheduledThreadPool(taskProperties.getThreadCount());
        this.taskProperties = taskProperties;
    }

    public void listenToCertificatesData() {
        AtomicInteger consumersCount = new AtomicInteger(0);
        List<FilesConsumer> consumers = new ArrayList<>(taskProperties.getThreadCount());
        for (int i = 0; i < taskProperties.getThreadCount(); i++) {
            consumers.add(
                    new FilesConsumer(queue, certificateRepository, tagRepository,
                            isScanning, taskProperties, consumersCount));
        }
        executorService.scheduleAtFixedRate(
                new FilesProducer(consumerService, consumers, queue, taskProperties, isScanning, consumersCount),
                taskProperties.getInitialDelay(), taskProperties.getScanDelay(), TimeUnit.MILLISECONDS);
    }

    @PreDestroy
    public void destroy() {
        executorService.shutdown();
        consumerService.shutdown();
    }
}
