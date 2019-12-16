package com.epam.esm.service;

import com.epam.esm.repository.Repository;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

/**
 * utility
 *
 * @author Dzmitry Platonov on 2019-12-13.
 * @version 0.0.1
 */
public class DataProcessingListener implements Callable<DataProcessingResult> {

    private Path rootPath;
    private Repository repository;

    public DataProcessingListener(Path rootPath, Repository repository) {
        this.rootPath = rootPath;
        this.repository = repository;
    }

    @Override
    public DataProcessingResult call() throws IOException, InterruptedException {
        System.out.println("data processor listening");
        System.out.println(Files.walk(rootPath).filter(path -> !Files.isDirectory(path)).count());

        TimeUnit.SECONDS.sleep(4);
        while (Files.walk(rootPath).anyMatch(path -> !Files.isDirectory(path))) {
//            System.out.println("files in root found");
            TimeUnit.SECONDS.sleep(2);
//            Thread.sleep(2000);
        }

        long errorFilesCounter =
                Files.walk(Path.of("../errors")).filter(path -> !Files.isDirectory(path)).count();

        return new DataProcessingResult(errorFilesCounter, repository.count());
    }

}
