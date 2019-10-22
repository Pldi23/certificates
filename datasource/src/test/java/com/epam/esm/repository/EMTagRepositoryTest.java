package com.epam.esm.repository;

import com.epam.esm.TestConfig;
import com.epam.esm.entity.Tag;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.annotation.DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD;

/**
 * gift-certificates
 *
 * @author Dzmitry Platonov on 2019-10-21.
 * @version 0.0.1
 */
@Transactional
@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = TestConfig.class)
class EMTagRepositoryTest {

    @Autowired
    EMTagRepository tagRepository;

    @Test
    @DisplayName("should save tag and return")
    void savePositive() {
        Tag tag = Tag.builder().title("test").build();
        Tag actual = tagRepository.save(tag);
        Tag expected = Tag.builder().id(1L).title("test").build();
        assertEquals(expected, actual);
    }

}