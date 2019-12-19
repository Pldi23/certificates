package com.epam.esm.service;

import com.epam.esm.exception.FolderException;
import com.epam.esm.listener.DataProcessingListener;
import com.epam.esm.listener.DataProcessingResult;
import com.epam.esm.process.Processor;
import com.epam.esm.process.ProcessorCharger;
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
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Stream;

/**
 * utility
 *
 * @author Dzmitry Platonov on 2019-12-12.
 * @version 0.0.1
 */
@Slf4j
public class FileService {

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

        long certificatesCount = repository.count();
        long errorsCount = countErrors();
        DataStatistic statistic = new DataStatistic();

        long finish = System.currentTimeMillis() + utilityConfiguration.getTestTime();
        Processor processor = new Processor(finish);

        processor.addContainer(rootAndSubPaths, statistic);
        new Thread(new ProcessorCharger(finish, processor, rootAndSubPaths, statistic)).start();
        processor.startNext();

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

    private long countErrors() {
        if (Files.exists(utilityConfiguration.getErrors())) {
            try (Stream<Path> pathStream = Files.walk(utilityConfiguration.getErrors())) {
                return pathStream.filter(path -> !Files.isDirectory(path)).count();
            } catch (IOException e) {
                log.warn("could not count error files ", e);
                return -1;
            }
        }
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
//        try {
//            Files.setAttribute(utilityConfiguration.getRoot(), "processed", false);
//        } catch (IOException e) {
//            throw new FolderException("Attribute could not be setted", e);
//        }
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
