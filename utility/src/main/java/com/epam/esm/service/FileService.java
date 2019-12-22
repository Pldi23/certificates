package com.epam.esm.service;

import com.epam.esm.exception.FolderException;
import com.epam.esm.exception.RepositoryException;
import com.epam.esm.listener.DataProcessingListener;
import com.epam.esm.listener.DataProcessingResult;
import com.epam.esm.process.ThreadStarter;
import com.epam.esm.properties.UtilityConfiguration;
import com.epam.esm.repository.Repository;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.Semaphore;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Stream;


@Slf4j
public class FileService {

    private static final int CREATORS_AWAIT_INTERVAL = 1;

    private Repository repository;
    private ExecutorService executorService;
    private UtilityConfiguration utilityConfiguration;

    public FileService(Repository repository) {
        this.utilityConfiguration = UtilityConfiguration.getInstance();
        this.repository = repository;
        this.executorService = Executors.newSingleThreadScheduledExecutor();
    }

    public PoolExecutionResult generateData() {

        deleteRootDir();
        createDirs();
        List<Path> rootAndSubPaths = getRootAndSubPaths();

        long certificatesCount = countCertificatesCurrentQuantity();
        long errorsCount = countErrors();
        DataStatistic statistic = new DataStatistic();
        AtomicInteger counter = new AtomicInteger(0);
        Semaphore semaphore = new Semaphore(rootAndSubPaths.size(), true);
        long finish = System.currentTimeMillis() + utilityConfiguration.getTestTime();
        ThreadStarter threadStarter = new ThreadStarter(rootAndSubPaths, finish, statistic, semaphore, counter);

        threadStarter.work();
        awaitCreators(counter, finish);

        return new PoolExecutionResult(statistic, certificatesCount, errorsCount);
    }

    public DataProcessingResult listenToFinishProcessing() {
        Future<DataProcessingResult> submit = executorService.submit(
                new DataProcessingListener(utilityConfiguration.getRoot(), utilityConfiguration.getErrors(), repository));
        try {
            return submit.get();
        } catch (InterruptedException e) {
            log.warn("interrupted", e);
            Thread.currentThread().interrupt();
            return new DataProcessingResult();
        } catch (ExecutionException e) {
            log.warn("exception during execution", e);
            return new DataProcessingResult();
        }
    }

    public void destroy() {
        executorService.shutdown();
    }



    private void awaitCreators(AtomicInteger creatorsCounter, long finish) {
        while (System.currentTimeMillis() <= finish || creatorsCounter.get() != 0) {
            log.info("waiting" + creatorsCounter.get());
            try {
                TimeUnit.MILLISECONDS.sleep(CREATORS_AWAIT_INTERVAL);
            } catch (InterruptedException e) {
                log.warn("interrupted", e);
                Thread.currentThread().interrupt();
            }
        }
    }

    private List<Path> getRootAndSubPaths() {
        List<Path> paths = new ArrayList<>();
        try (Stream<Path> pathStream = Files.walk(utilityConfiguration.getRoot())) {
            pathStream
                    .sorted(Comparator.reverseOrder())
                    .filter(path -> path.toFile().isDirectory())
                    .forEach(paths::add);
        } catch (IOException e) {
            log.warn("could not get root or sub-paths ", e);
            throw new UncheckedIOException(e);
        }
        return paths;
    }

    private long countCertificatesCurrentQuantity() {
        try {
            return repository.count();
        } catch (RepositoryException e) {
            log.warn("could not count certificates current quantity");
            return -1;
        }
    }

    private long countErrors() {
        if (Files.exists(utilityConfiguration.getErrors())) {
            try (Stream<Path> pathStream = Files.walk(utilityConfiguration.getErrors())) {
                return pathStream.filter(path -> !Files.isDirectory(path)).count();
            } catch (IOException e) {
                log.warn("could not count error files ", e);
                return -1;
            }
        }
        log.warn("folder not exist, so quantity of errors is 0 ");
        return 0;
    }

    private void createDirs() {
        generatePath().forEach(l -> {
            try {
                if (!Files.exists(getPath(l))) {
                    Files.createDirectories(getPath(l));
                }
            } catch (IOException e) {
                throw new FolderException("Folder could not be created", e);
            }
        });
    }

    private void deleteRootDir() {
        if (Files.exists(utilityConfiguration.getRoot())) {
            try (Stream<Path> pathStream = Files.walk(utilityConfiguration.getRoot())) {
                pathStream
                        .sorted(Comparator.reverseOrder())
                        .forEach(path -> {
                            try {
                                Files.deleteIfExists(path);
                            } catch (IOException e) {
                                throw new FolderException("Folder - " + path + " could not be deleted", e);
                            }
                        });
            } catch (IOException e) {
                throw new FolderException("Folder could not be deleted", e);
            }

        }
    }

    private List<String> folderNames() {
        List<String> strings = new ArrayList<>();
        for (int i = 0; i < utilityConfiguration.getSubFoldersCount(); i++) {
            strings.add("sub_folder_" + i);
        }
        return strings;
    }

    private List<List<String>> generatePath() {
        List<String> folders = folderNames();
        List<List<String>> paths = new ArrayList<>();
        while (!folders.isEmpty()) {
            List<String> path = new ArrayList<>();
            int nextInt = ThreadLocalRandom.current().nextInt(folders.size() + 1);
            while (nextInt > 50) {
                nextInt = ThreadLocalRandom.current().nextInt(folders.size() + 1);
            }
            for (int i = 0; i < nextInt; i++) {
                path.add(folders.get(i));
            }
            folders.removeIf(path::contains);
            paths.add(path);
        }
        return paths;
    }

    private Path getPath(List<String> folders) {
        String[] strings = new String[folders.size()];
        for (int i = 0; i < folders.size(); i++) {
            strings[i] = folders.get(i);
        }
        return Path.of(utilityConfiguration.getRoot().toString(), strings);
    }
}
