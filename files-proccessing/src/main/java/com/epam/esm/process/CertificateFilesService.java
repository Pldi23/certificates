package com.epam.esm.process;

import com.epam.esm.repository.AbstractCertificateRepository;
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

    private AbstractCertificateRepository certificateRepository;
    private LinkedTransferQueue<Path> queue;
    private AtomicBoolean isScanning;
    private ScheduledExecutorService executorService;
    private ExecutorService consumerService;
    private TaskProperties taskProperties;

    public CertificateFilesService(AbstractCertificateRepository certificateRepository,
                                   TaskProperties taskProperties) {
        this.queue = new LinkedTransferQueue<>();
        this.certificateRepository = certificateRepository;
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
                    new FilesConsumer(queue, certificateRepository,
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
