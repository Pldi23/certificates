package com.epam.esm.repository;

import com.epam.esm.DatabaseSetupExtension;
import com.epam.esm.entity.Tag;
import com.epam.esm.specification.FindAllTagSpecification;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.Optional;

import static org.junit.Assert.assertEquals;

/**
 * gift certificates
 *
 * @author Dzmitry Platonov on 2019-09-23.
 * @version 0.0.1
 */

public class TagRepositoryTest extends DatabaseSetupExtension {

    @Autowired
    private Repository<Tag> tagRepository;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Test
    public void testAddPositive() {
        tagRepository.add(new Tag(9L, "test"));
        assertEquals(8, (int) jdbcTemplate.queryForObject("select count(*) from tag", Integer.class));
    }

    @Test
    public void testRemovePositive() {
        tagRepository.remove(7);
        assertEquals(6, (int) jdbcTemplate.queryForObject("select count(*) from tag", Integer.class));
    }

    @Test
    public void testQuery() {
        assertEquals(7, tagRepository.query(new FindAllTagSpecification()).size());
    }

    @Test
    public void testFindOne() {
        Tag actual = tagRepository.findOne(1).get();
        Tag expected = new Tag(1L, "extreme");
        assertEquals(expected, actual);
    }

    @Test
    public void testFindOneEmpty() {
        assertEquals(Optional.empty(), tagRepository.findOne(200));
    }

}