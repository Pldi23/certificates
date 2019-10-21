package com.epam.esm.controller;

import com.epam.esm.generate.RandomDataGenerator;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * gift-certificates
 *
 * @author Dzmitry Platonov on 2019-10-21.
 * @version 0.0.1
 */
@RestController
@RequestMapping("generate")
public class DataController {

    private static final int TAGS_QUANTITY = 1000;
    private static final int CERTIFICATES_QUANTITY = 10000;
    private static final int USERS_QUANTITY = 1000;
    private static final int ORDERS_QUANTITY = 1000;

    private RandomDataGenerator randomDataGenerator;

    public DataController(RandomDataGenerator randomDataGenerator) {
        this.randomDataGenerator = randomDataGenerator;
    }

    @PostMapping
    public ResponseEntity generateData() {
        randomDataGenerator.generate(TAGS_QUANTITY, CERTIFICATES_QUANTITY, USERS_QUANTITY, ORDERS_QUANTITY);

        return ResponseEntity.ok().build();
    }
}
