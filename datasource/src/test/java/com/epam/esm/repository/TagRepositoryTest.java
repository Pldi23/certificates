package com.epam.esm.repository;

import com.epam.esm.TestConfig;
import com.epam.esm.entity.Tag;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.assertEquals;


@Transactional
@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = TestConfig.class)
class TagRepositoryTest {

    @Autowired
    TagRepository tagRepository;

    @Test
    @DisplayName("should save tag and return")
    void savePositive() {
        Tag tag = Tag.builder().title("test").build();
        Tag actual = tagRepository.save(tag);
        Tag expected = Tag.builder().id(1L).title("test").build();
        assertEquals(expected, actual);
    }

}