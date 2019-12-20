package com.epam.esm.service.processing;


import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.entity.Tag;
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
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.LinkedTransferQueue;
import java.util.concurrent.atomic.AtomicBoolean;
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

    public FilesConsumer(LinkedTransferQueue<Path> queue,
                         JpaCertificateRepository certificateRepository,
                         JpaTagRepository tagRepository,
                         AtomicBoolean isProducing,
                         TaskProperties taskProperties) {
        this.queue = queue;
        this.certificateRepository = certificateRepository;
        this.tagRepository = tagRepository;
        this.isProducing = isProducing;
        this.taskProperties = taskProperties;

    }

    @Override
    public void run() {
        log.info("consumer starts");
        while (isProducing.get()) {
            try {
                processFile(queue.take());
            } catch (IOException e) {
                log.warn("consumer ", e);
            } catch (InterruptedException e) {
                log.warn("interrupted", e);
                Thread.currentThread().interrupt();
            }
        }
        log.info("consumer finished");
    }

    private void processFile(Path path) throws IOException {
        path.toFile().setExecutable(false);
        log.info(path + " processing");
        try {
            ObjectMapper mapper = new ObjectMapper();
            List<GiftCertificate> giftCertificates = mapper.readValue(path.toFile(), new TypeReference<List<GiftCertificate>>() {
            });
            ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
            Validator validator = factory.getValidator();
            Set<ConstraintViolation<GiftCertificate>> violations = new HashSet<>();
            giftCertificates.forEach(giftCertificate -> violations.addAll(validator.validate(giftCertificate)));
            if (violations.isEmpty()) {
//                Set<Tag> tags = new HashSet<>();
//                giftCertificates.forEach(giftCertificate -> tags.addAll(giftCertificate.getTags()));
//                tagRepository.saveAll(tags);
                saveTags(giftCertificates);
                certificateRepository.saveAll(giftCertificates);
                Files.deleteIfExists(path);

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
        log.info(path + " processing finished");
    }

    private void saveTags(List<GiftCertificate> giftCertificates) {
//        Set<Tag> tags = new HashSet<>();
        giftCertificates.forEach(giftCertificate -> giftCertificate.setTags(tagRepository.saveOrMergeAll(giftCertificate.getTags())));
//        return tagRepository.saveAll(tags);
    }

    private void moveInvalidFile(Path targetFolder, Path path) throws IOException {
        if (!Files.exists(Path.of(taskProperties.getErrorFolder()))) {
            try {
                lockErrorFolderCreation.lock();
                if (!Files.exists(Path.of(taskProperties.getErrorFolder()))) {
                    Files.createDirectory(Path.of(taskProperties.getErrorFolder()));
                }
            } finally {
                lockErrorFolderCreation.unlock();
            }
        }
        if (!Files.exists(targetFolder)) {
            try {
                lockInnerFolderCreation.lock();
                if (!Files.exists(targetFolder)) {
                    Files.createDirectory(targetFolder);
                }
            } finally {
                lockInnerFolderCreation.unlock();
            }
        }
        File file = new File(String.format("%s/%s-%s", targetFolder, path.getFileName(), System.currentTimeMillis()));
        Files.move(path, file.toPath());
        Files.deleteIfExists(path);
    }

}
