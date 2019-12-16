package com.epam.esm.controller;

import com.epam.esm.constant.EndPointConstant;
import com.epam.esm.constant.RoleConstant;
import com.epam.esm.generate.RandomDataGenerator;
import com.epam.esm.service.processing.FilesCreationResult;
import com.epam.esm.service.processing.FolderCreatorService;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Comparator;

/**
 * to generate test data
 *
 * @author Dzmitry Platonov on 2019-10-21.
 * @version 0.0.1
 */
@Log4j2
@RestController
@RequestMapping(EndPointConstant.DATA_GENERATOR_ENDPOINT)
public class DataController {

    private static final int TAGS_QUANTITY = 1000;
    private static final int CERTIFICATES_QUANTITY = 10000;
    private static final int USERS_QUANTITY = 1000;
    private static final int ORDERS_QUANTITY = 1000;

    private RandomDataGenerator randomDataGenerator;
    private FolderCreatorService service;

    public DataController(RandomDataGenerator randomDataGenerator, FolderCreatorService service) {
        this.randomDataGenerator = randomDataGenerator;
        this.service = service;
    }

    @Secured(RoleConstant.ROLE_ADMIN)
    @PostMapping
    public ResponseEntity generateData() {
        randomDataGenerator.generate(TAGS_QUANTITY, CERTIFICATES_QUANTITY, USERS_QUANTITY, ORDERS_QUANTITY);

        return ResponseEntity.ok().build();
    }

    @Secured(RoleConstant.ROLE_ADMIN)
    @PostMapping("/folder")
    public ResponseEntity generateFolder() {
        FilesCreationResult filesCreationResult = service.createFolder();
        try {
//            log.info("errors folder files count" + Files.walk(Path.of("/errors"))
//                    .sorted(Comparator.reverseOrder())
//                    .filter(path -> path.toFile().isDirectory())
//                    .mapToInt(p -> 1)
//                    .sum());
            log.info("task folder files count" + Files.walk(Path.of("task5folder"))
                    .sorted(Comparator.reverseOrder())
                    .filter(path -> !path.toFile().isDirectory())
                    .mapToInt(p -> 1)
                    .sum());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return ResponseEntity.ok().body(filesCreationResult);
    }

    @Secured(RoleConstant.ROLE_ADMIN)
    @DeleteMapping("/folder")
    public ResponseEntity deleteGeneratedFolder() {
        service.deleteFolder();
        return ResponseEntity.status(204).build();
    }
}
