package com.epam.esm.process;

import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.concurrent.atomic.AtomicBoolean;


public class CheckRootFileVisitor extends SimpleFileVisitor<Path> {

    private AtomicBoolean isScanning;

    CheckRootFileVisitor(AtomicBoolean isScanning) {
        this.isScanning = isScanning;
    }

    @Override
    public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) {
        if (Files.isRegularFile(file) && file.toFile().canExecute()) {
            isScanning.set(true);
            return FileVisitResult.TERMINATE;
        }
        return FileVisitResult.CONTINUE;
    }
}
