package com.epam.esm.process;


import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.repository.JpaCertificateRepository;
import com.epam.esm.repository.JpaTagRepository;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.exc.MismatchedInputException;
import com.fasterxml.jackson.databind.exc.UnrecognizedPropertyException;
import lombok.extern.log4j.Log4j2;
import org.springframework.dao.DataIntegrityViolationException;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.LinkedTransferQueue;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

@Log4j2
public class FilesConsumer implements Runnable {

    private static final ReentrantLock lockErrorFolderCreation = new ReentrantLock();
    private static final ReentrantLock lockInnerFolderCreation = new ReentrantLock();

    private LinkedTransferQueue<Path> queue;
    private JpaCertificateRepository certificateRepository;
    private JpaTagRepository tagRepository;
    private AtomicBoolean isProducing;
    private TaskProperties taskProperties;
    private AtomicInteger consumersCount;

    FilesConsumer(LinkedTransferQueue<Path> queue,
                         JpaCertificateRepository certificateRepository,
                         JpaTagRepository tagRepository,
                         AtomicBoolean isProducing,
                         TaskProperties taskProperties, AtomicInteger consumersCount) {
        this.queue = queue;
        this.certificateRepository = certificateRepository;
        this.tagRepository = tagRepository;
        this.isProducing = isProducing;
        this.taskProperties = taskProperties;
        this.consumersCount = consumersCount;

    }

    @Override
    public void run() {
        ObjectMapper mapper = new ObjectMapper();
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();
        while (isProducing.get()) {
            try {
                processFile(queue.take(), mapper, validator);
            } catch (IOException e) {
                log.warn("consumer could not process file", e);
            } catch (InterruptedException e) {
                log.warn("interrupted", e);
                consumersCount.decrementAndGet();
                Thread.currentThread().interrupt();
            }
        }
        log.info("consumer finished");
    }

    private void processFile(Path path, ObjectMapper mapper, Validator validator) throws IOException {
        try {
            List<GiftCertificate> giftCertificates = mapper.readValue(path.toFile(), new TypeReference<List<GiftCertificate>>() {
            });
            if (validate(giftCertificates, validator).isEmpty()) {
                saveTags(giftCertificates);
                certificateRepository.saveAll(giftCertificates);
                Files.delete(path);
            } else {
                moveInvalidFile(Path.of(taskProperties.getErrorValidatorViolationsFolder()), path);
            }
        } catch (DataIntegrityViolationException ex) {
            moveInvalidFile(Path.of(taskProperties.getErrorDataIntegrityFolder()), path);
        } catch (UnrecognizedPropertyException ex) {
            moveInvalidFile(Path.of(taskProperties.getErrorJsonMappingFolder()), path);
        } catch (MismatchedInputException ex) {
            moveInvalidFile(Path.of(taskProperties.getErrorMismatchInputFolder()), path);
        }
    }

    private void saveTags(List<GiftCertificate> giftCertificates) {
        giftCertificates.forEach(giftCertificate -> giftCertificate.setTags(tagRepository.saveOrMergeAll(giftCertificate.getTags())));
    }

    private void moveInvalidFile(Path targetFolder, Path path) throws IOException {
        createFolderIfNotExists(Path.of(taskProperties.getErrorFolder()), lockErrorFolderCreation);
        createFolderIfNotExists(targetFolder, lockInnerFolderCreation);
        File file = new File(String.format("%s/%s-%s", targetFolder, path.getFileName(), LocalDateTime.now()));
        Files.move(path, file.toPath());
        Files.deleteIfExists(path);
    }

    private Set<ConstraintViolation<GiftCertificate>> validate(List<GiftCertificate> giftCertificates, Validator validator) {
        Set<ConstraintViolation<GiftCertificate>> violations = new HashSet<>();
        giftCertificates.forEach(giftCertificate -> violations.addAll(validator.validate(giftCertificate)));
        return violations;
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
