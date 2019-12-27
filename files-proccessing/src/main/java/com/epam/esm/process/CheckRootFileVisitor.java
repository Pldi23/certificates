package com.epam.esm.process;

import org.springframework.stereotype.Component;

import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;

@Component
public class CheckRootFileVisitor extends SimpleFileVisitor<Path> {

    private SystemMonitor systemMonitor;
    private TaskProperties taskProperties;

    public CheckRootFileVisitor(SystemMonitor systemMonitor, TaskProperties taskProperties) {
        this.systemMonitor = systemMonitor;
        this.taskProperties = taskProperties;
    }

    @Override
    public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) {
        if (Files.isRegularFile(file) && file.toFile().canExecute()
                && !file.toString().contains(taskProperties.getMarkerFileName())
                && !file.toString().contains(".DS_Store")) {
            systemMonitor.activateProducer();
            return FileVisitResult.TERMINATE;
        }
        return FileVisitResult.CONTINUE;
    }
}
