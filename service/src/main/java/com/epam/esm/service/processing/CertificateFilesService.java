package com.epam.esm.service.processing;

import com.epam.esm.converter.CertificateConverter;
import com.epam.esm.converter.TagConverter;
import com.epam.esm.repository.JpaCertificateRepository;
import com.epam.esm.repository.JpaTagRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import javax.annotation.PreDestroy;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedTransferQueue;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Log4j2
@Service
public class CertificateFilesService {

    private CertificateConverter certificateConverter;
    private JpaCertificateRepository certificateRepository;
    private JpaTagRepository tagRepository;
    private TagConverter tagConverter;
    private LinkedTransferQueue<Path> queue;
    private AtomicBoolean isScanning;
    private ScheduledExecutorService executorService;
    private ExecutorService consumerService;
    private TaskProperties taskProperties;

    public CertificateFilesService(CertificateConverter certificateConverter, JpaCertificateRepository certificateRepository,
                                   JpaTagRepository tagRepository, TagConverter tagConverter,
                                   TaskProperties taskProperties) {
        this.certificateConverter = certificateConverter;
        this.certificateRepository = certificateRepository;
        this.tagRepository = tagRepository;
        this.tagConverter = tagConverter;
        this.queue = new LinkedTransferQueue<>();
        this.isScanning = new AtomicBoolean();
        this.executorService = Executors.newSingleThreadScheduledExecutor();
        this.consumerService = Executors.newScheduledThreadPool(taskProperties.getThreadCount());
        this.taskProperties = taskProperties;
    }

    public void listenToCertificatesData() {
//        long scanSeconds = (long) (taskProperties.getScanDelay() * 10); //0.01 seconds
//        log.info("scan seconds " + scanSeconds);
        executorService.scheduleAtFixedRate(producer, 1, 100, TimeUnit.MILLISECONDS);
    }

    private Runnable producer = () -> {
//        log.info("listen to data executed");
        Path path = Path.of(taskProperties.getFolder());
        if (path.toFile().exists() && path.toFile().isDirectory() && containsFiles(path)) {
            isScanning.set(true);
            int consumersCount = taskProperties.getThreadCount();
//            log.info(consumersCount + "consumers starting");
            for (int i = 0; i < consumersCount; i++) {
                consumerService.execute(new FilesConsumer(queue, certificateRepository, tagRepository, certificateConverter,
                        tagConverter, isScanning, taskProperties));
            }
//            log.info("start to produce");
            while (true) {
                try {
                    List<Path> paths = Files.walk(path)
                            .sorted(Comparator.reverseOrder())
                            .filter(p -> p.toFile().isFile() && p.toFile().canExecute())
                            .collect(Collectors.toList());
                   if (paths.isEmpty()) {
//                       log.info("paths is empty -> breaking");
                       break;
                   }
                    for (Path p : paths) {
//                        log.info(p + " producing");
                        queue.transfer(p);
                    }
                } catch (IOException e) {
                    log.warn("IO caught during producing", e);
                } catch (InterruptedException e) {
                    log.warn("interrupted thread caught", e);
                    Thread.currentThread().interrupt();
                }
            }
//            log.info("producer finished");
            isScanning.set(false);
            try {
                consumerService.awaitTermination(3, TimeUnit.SECONDS);
            } catch (InterruptedException e) {
                log.warn("interrupted thread caught", e);
                Thread.currentThread().interrupt();
            }
//            consumerService.shutdown();
        }
    };

//    private Runnable scanner = () -> {
//        log.info("listen to data executed");
//        if (Files.exists(Path.of(taskProperties.getFolder())) && checkTaskFolderForFiles()) {
//            log.info("start to scan");
//            isScanning.set(true);
////            while (isScanning.get()) {
//            executorService.execute(new FilesProducer(Path.of(taskProperties.getFolder()), queue, isScanning));
//            int consumersCount = taskProperties.getThreadCount() - 1;
//            for (int i = 0; i < consumersCount; i++) {
//                executorService.execute(new FilesConsumer(queue, certificateRepository, tagRepository, certificateConverter,
//                        tagConverter, isScanning, taskProperties));
//            }
////            }
//        }
//    };

    private boolean containsFiles(Path path) {
        try {
            Stream<Path> stream = Files.walk(path);
            return stream.anyMatch(p -> !Files.isDirectory(p));
        } catch (IOException e) {
            return false;
        }
    }

    private boolean checkTaskFolderForFiles() {
        try (Stream<Path> pathStream = Files.list(Path.of(taskProperties.getFolder()))) {
            return pathStream.anyMatch(path -> !Files.isDirectory(path)) && !isScanning.get();
        } catch (IOException e) {
            log.info("could not check folder", e);
            return false;
        }
    }

    @PreDestroy
    public void destroy() {
        executorService.shutdown();
        consumerService.shutdown();
    }

}
