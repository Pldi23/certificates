package com.epam.esm.service.processing;

import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * gift-certificates
 *
 * @author Dzmitry Platonov on 2019-12-19.
 * @version 0.0.1
 */
public class CheckRootFileVisitor extends SimpleFileVisitor<Path> {

    private AtomicBoolean isScanning;

    public CheckRootFileVisitor(AtomicBoolean isScanning) {
        this.isScanning = isScanning;
    }

    @Override
    public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
        if (Files.isRegularFile(file) && file.toFile().canExecute()) {
            isScanning.set(true);
            return FileVisitResult.TERMINATE;
        }
        return FileVisitResult.CONTINUE;
    }
}
