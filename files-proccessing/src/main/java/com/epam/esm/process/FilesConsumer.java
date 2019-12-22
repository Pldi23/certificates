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
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.LinkedTransferQueue;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.Collectors;

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
        while (isProducing.get()) {
            try {
                processFile(queue.take(), mapper);
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

    private void processFile(Path path, ObjectMapper mapper) throws IOException {
        try {
            List<GiftCertificate> giftCertificates = mapper.readValue(path.toFile(), new TypeReference<List<GiftCertificate>>() {
            });
            if (validate(giftCertificates).isEmpty()) {
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

    private Set<ConstraintViolation<GiftCertificate>> validate(List<GiftCertificate> giftCertificates) {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();
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

    private List<GiftCertificate> readFile(Path path) {
        try {
            String string = Files.readString(path);
            String[] certificateStrings = string.split("},\\{");
            return Arrays.stream(certificateStrings).map(this::toCertificate).collect(Collectors.toList());
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
//[{"id":null,"name":"df7fab53-f001-463b-9150-4f92f0668d02","description":"description","price":1,"creationDate":null,"modificationDate":null,"expirationDate":"18/12/2020","activeStatus":null,"tags":[{"title": "a"},{"title": "b"},{"title": "c"}]},{"id":null,"name":"d17d348e-89cf-440a-baee-721cb150f5b2","description":"description","price":1,"creationDate":null,"modificationDate":null,"expirationDate":"18/12/2020","activeStatus":null,"tags":[{"title": "a"},{"title": "b"}]},{"id":null,"name":"41868f4c-3572-40c6-bcdf-76cf632c6633","description":"description","price":1,"creationDate":null,"modificationDate":null,"expirationDate":"18/12/2020","activeStatus":null,"tags":[{"title": "a"}]}]
    private GiftCertificate toCertificate(String certificateString) {
        String replaced = certificateString.replaceAll("\"", "");
        String[] elements = replaced.split(",");
        Map<String, String > fields = new HashMap<>();
        for (String s: elements) {
            String[] fieldEntry = s.split(":");
            fields.put(fieldEntry[0], fieldEntry[1]);
        }
        return GiftCertificate.builder()
                .id(Long.parseLong(fields.get("id")))
                .name(fields.get("name"))
                .description(fields.get("description"))
                .price(new BigDecimal(fields.get("price")))
                .creationDate(LocalDate.parse(fields.get("creationDate")))
                .modificationDate(LocalDate.parse(fields.get("modificationDate")))
                .expirationDate(LocalDate.parse(fields.get("expirationDate")))
                .activeStatus(Boolean.parseBoolean(fields.get("activeStatus")))
                .tags()
                .build();
    }

}
