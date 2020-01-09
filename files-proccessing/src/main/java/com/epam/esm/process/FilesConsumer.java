package com.epam.esm.process;


import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.repository.AbstractCertificateRepository;
import com.epam.esm.validate.ValidationException;
import com.epam.esm.validate.Validator;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.exc.MismatchedInputException;
import com.fasterxml.jackson.databind.exc.UnrecognizedPropertyException;
import lombok.extern.log4j.Log4j2;
import org.springframework.dao.DataIntegrityViolationException;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.LinkedTransferQueue;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

@Log4j2
public class FilesConsumer implements Runnable {

    private static final ReentrantLock lockErrorFolderCreation = new ReentrantLock();
    private static final ReentrantLock lockInnerFolderCreation = new ReentrantLock();

    private LinkedTransferQueue<Path> queue;
    private AbstractCertificateRepository certificateRepository;
    private SystemMonitor systemMonitor;
    private TaskProperties taskProperties;
    private Validator validator;
    private ObjectMapper mapper;

    FilesConsumer(LinkedTransferQueue<Path> queue,
                  AbstractCertificateRepository certificateRepository,
                  SystemMonitor systemMonitor,
                  TaskProperties taskProperties, Validator validator, ObjectMapper mapper) {
        this.queue = queue;
        this.certificateRepository = certificateRepository;
        this.systemMonitor = systemMonitor;
        this.taskProperties = taskProperties;
        this.validator = validator;
        this.mapper = mapper;

    }

    @Override
    public void run() {
        while (systemMonitor.getStatus()) {
            try {
                Path path = queue.take();
                if (path.toString().contains(taskProperties.getStopConsumerMarker())) {
                    return;
                }
                processFile(path);
            } catch (IOException e) {
                log.warn("consumer could not process file", e);
            } catch (InterruptedException e) {
                log.warn("interrupted", e);
                systemMonitor.decrementActiveConsumersCounter();
                Thread.currentThread().interrupt();
            }
        }
    }

    private void processFile(Path path) throws IOException {
        try {
            List<GiftCertificate> giftCertificates = mapper.readValue(path.toFile(), new TypeReference<List<GiftCertificate>>() {
            });
            validator.validate(giftCertificates);
            saveCertificatesList(giftCertificates, path);
        } catch (ValidationException ex) {
            moveInvalidFile(Path.of(taskProperties.getErrorValidatorViolationsFolder()), path);
        } catch (DataIntegrityViolationException ex) {
            moveInvalidFile(Path.of(taskProperties.getErrorDataIntegrityFolder()), path);
        } catch (UnrecognizedPropertyException ex) {
            moveInvalidFile(Path.of(taskProperties.getErrorJsonMappingFolder()), path);
        } catch (MismatchedInputException ex) {
            moveInvalidFile(Path.of(taskProperties.getErrorMismatchInputFolder()), path);
        }
    }


    private void saveCertificatesList(List<GiftCertificate> giftCertificates, Path path) throws IOException {
        if (certificateRepository.saveMany(giftCertificates)) {
            Files.delete(path);
        } else {
            moveInvalidFile(Path.of(taskProperties.getErrorDataIntegrityFolder()), path);
        }
    }


    private void moveInvalidFile(Path targetFolder, Path path) throws IOException {
        createFolderIfNotExists(Path.of(taskProperties.getErrorFolder()), lockErrorFolderCreation);
        createFolderIfNotExists(targetFolder, lockInnerFolderCreation);
        File file = new File(String.format("%s/%s-%s", targetFolder, path.getFileName(), LocalDateTime.now()));
        Files.move(path, file.toPath());
        Files.deleteIfExists(path);
    }

    private void createFolderIfNotExists(Path targetFolder, Lock lock) throws IOException {
        if (!Files.exists(targetFolder)) {
            try {
                lock.lock();
                if (!Files.exists(targetFolder)) {
                    Files.createDirectory(targetFolder);
                }
            } finally {
                lock.unlock();
            }
        }
    }

}
