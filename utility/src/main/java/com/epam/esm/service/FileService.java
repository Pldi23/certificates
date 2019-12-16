package com.epam.esm.service;

import com.epam.esm.exception.FolderException;
import com.epam.esm.repository.Repository;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
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
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * utility
 *
 * @author Dzmitry Platonov on 2019-12-12.
 * @version 0.0.1
 */
@Slf4j
public class FileService {

    private static final Path ROOT = Path.of("root");
    private static final Path ERRORS_PATH = Path.of("../errors");
    private static final int SUB_FOLDERS_COUNT = 9;

    private Repository repository;
    private ExecutorService executorService;

    public FileService(Repository repository) {
        this.repository = repository;
        executorService = Executors.newFixedThreadPool(SUB_FOLDERS_COUNT + 1);
    }

    public PoolExecutionResult generateData() throws IOException, InterruptedException {
        deleteRootDir();
        createDirs();
        List<Path> paths = new ArrayList<>();
        try (Stream<Path> pathStream = Files.walk(ROOT)) {
            pathStream
                    .sorted(Comparator.reverseOrder())
                    .filter(path -> path.toFile().isDirectory())
                    .forEach(paths::add);
        }

        long certificatesCount = repository.count();
        long errorsCount = 0;
        if (Files.exists(ERRORS_PATH)) {
            try (Stream<Path> pathStream = Files.walk(ERRORS_PATH)) {
                errorsCount = pathStream.filter(path -> !Files.isDirectory(path)).count();
            }
        }
        long finish = System.currentTimeMillis() + 1000;

        List<Future<ThreadExecutionResult>> futures = executorService.invokeAll(
                paths.stream()
                        .map(path -> new FilesCreator(path, finish))
                        .collect(Collectors.toList()));

//        return new PoolExecutionResult(VALID_CERTIFICATES_COUNTER.get() / 3 , INVALID_FILES_COUNTER.get(),
//                count + VALID_CERTIFICATES_COUNTER.get());

        return new PoolExecutionResult(futures.stream().map(future -> {
            try {
                return future.get();
            } catch (InterruptedException e) {
                return new ThreadExecutionResult();
            } catch (ExecutionException e) {
                return new ThreadExecutionResult();
            }
        }).collect(Collectors.toList()), certificatesCount, errorsCount);
    }

    public DataProcessingResult listenToFinishProcessing() {
        //        ScheduledExecutorService scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();
//        Future<DataProcessingResult> submit = scheduledExecutorService.schedule(new DataProcessingListener(ROOT, repository), 1000, TimeUnit.MILLISECONDS);

        Future<DataProcessingResult> submit = executorService.submit(new DataProcessingListener(ROOT, repository));
        try {

            return submit.get();
        } catch (InterruptedException e) {
            return new DataProcessingResult();
        } catch (ExecutionException e) {
            return new DataProcessingResult();
        }
    }

    public void destroy() {
        deleteRootDir();
        executorService.shutdown();
    }

    private void createDirs() {
        generatePath().forEach(l -> {
            try {
                Files.createDirectories(getPath(l));
            } catch (IOException e) {
                throw new FolderException("Folder could not be created", e);
            }
        });
    }

    private void deleteRootDir() {
        if (Files.exists(ROOT)) {
            try (Stream<Path> pathStream = Files.walk(ROOT)) {
                pathStream
                        .sorted(Comparator.reverseOrder())
//                        .map(Path::toFile)
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
        for (int i = 0; i < 9; i++) {
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
        return Path.of("root", strings);
    }
}
