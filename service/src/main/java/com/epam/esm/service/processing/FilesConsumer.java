package com.epam.esm.service.processing;


import com.epam.esm.converter.CertificateConverter;
import com.epam.esm.converter.TagConverter;
import com.epam.esm.dto.GiftCertificateDTO;
import com.epam.esm.dto.TagDTO;
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
import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.LinkedTransferQueue;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.Collectors;

@Log4j2
public class FilesConsumer implements Runnable {

    private static final ReentrantLock lockErrorFolderCreation = new ReentrantLock();
    private static final ReentrantLock lockInnerFolderCreation = new ReentrantLock();

    private LinkedTransferQueue<Path> queue;
    private JpaCertificateRepository certificateRepository;
    private JpaTagRepository tagRepository;
    private CertificateConverter certificateConverter;
    private TagConverter tagConverter;
    private AtomicBoolean isScanning;

    private TaskProperties taskProperties;

    public FilesConsumer(LinkedTransferQueue<Path> queue,
                         JpaCertificateRepository certificateRepository,
                         JpaTagRepository tagRepository,
                         CertificateConverter certificateConverter,
                         TagConverter tagConverter,
                         AtomicBoolean isScanning,
                         TaskProperties taskProperties) {
        this.queue = queue;
        this.certificateRepository = certificateRepository;
        this.tagRepository = tagRepository;
        this.certificateConverter = certificateConverter;
        this.tagConverter = tagConverter;
        this.isScanning = isScanning;
        this.taskProperties = taskProperties;
    }

    @Override
    public void run() {
        log.info("consumer starts");
        while (isScanning.get()) {
//            log.info(isScanning.get());
            try {
                Path path = queue.take();
                processFile(path);
            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
            }
        }
        log.info("consumer finished");

    }

    private void processFile(Path path) throws IOException {
        log.info(path + " processing");
        try {
            ObjectMapper mapper = new ObjectMapper();
//            Gson gson = new Gson();
            if (path.toFile().canExecute()) {
                path.toFile().setExecutable(false);
//                Type listOfMyClassObject = new TypeToken<List<GiftCertificateDTO>>() {
//                }.getType();
//                List<GiftCertificateDTO> giftCertificateDTOs;
//                try (FileReader reader = new FileReader(path.toString())) {
//                    giftCertificateDTOs = gson.fromJson(reader, listOfMyClassObject);
//                }
                List<GiftCertificateDTO> giftCertificateDTOs = mapper.readValue(path.toFile(), new TypeReference<List<GiftCertificateDTO>>() {
                });
                ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
                Validator validator = factory.getValidator();
                Set<ConstraintViolation<GiftCertificateDTO>> violations
                        = new HashSet<>();
                giftCertificateDTOs.forEach(giftCertificateDTO -> violations.addAll(validator.validate(giftCertificateDTO)));
                if (violations.isEmpty()) {
                    List<GiftCertificate> list = giftCertificateDTOs.stream().map(giftCertificateDTO -> {
                        Set<Tag> tags = saveTags(giftCertificateDTO.getTags());
                        GiftCertificate giftCertificate = certificateConverter.convert(giftCertificateDTO);
                        giftCertificate.setCreationDate(LocalDate.now());
                        giftCertificate.setActiveStatus(true);
                        giftCertificate.setTags(tags);
                        return giftCertificate;
                    }).collect(Collectors.toList());
                    certificateRepository.saveAll(list);
                    Files.deleteIfExists(path);
                } else {
                    moveInvalidFile(Path.of(taskProperties.getErrorValidatorViolationsFolder()), path);
                }

            } else {
                log.info(path + " not executable");
            }
        } catch (DataIntegrityViolationException ex) {
            moveInvalidFile(Path.of(taskProperties.getErrorDataIntegrityFolder()), path);
        } catch (UnrecognizedPropertyException ex) {
            log.info(ex);
            moveInvalidFile(Path.of(taskProperties.getErrorJsonMappingFolder()), path);
        } catch (MismatchedInputException ex) {
            log.info(ex);
            moveInvalidFile(Path.of(taskProperties.getErrorMismatchInputFolder()), path);
        }
        log.info(path + " processing finished");
    }

    private Set<Tag> saveTags(Set<TagDTO> tagDTOs) {
        return new HashSet<>(tagRepository.saveAll(tagDTOs.stream().map(t -> tagConverter.convert(t)).collect(Collectors.toSet())));
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
        File file = new File(String.format("%s/%s-%s", targetFolder, path.getFileName(), UUID.randomUUID()));
        Files.move(path, file.toPath());
        Files.deleteIfExists(path);
    }

}
