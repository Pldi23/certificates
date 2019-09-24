package com.epam.esm.repository;

import com.epam.esm.AppConfig;
import com.epam.esm.entity.Tag;
import com.epam.esm.repository.specification.FindAllTagSpecification;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.junit.jupiter.api.Assertions.*;

/**
 * gift certificates
 *
 * @author Dzmitry Platonov on 2019-09-23.
 * @version 0.0.1
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {AppConfig.class})
public class TagRepositoryTest extends DatabaseSetupExtension {

    @Autowired
    private Repository<Tag> tagRepository;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Test
    public void testAddPositive() {
        tagRepository.add(new Tag("test"));
        assertEquals(8, (int) jdbcTemplate.queryForObject("select count(*) from tag", Integer.class));
    }

    @Test
    public void testRemovePositive() {
        tagRepository.remove(new Tag("jump"));
        assertEquals(6, (int) jdbcTemplate.queryForObject("select count(*) from tag", Integer.class));
    }

    @Test
    public void testQuery() {
        assertEquals(7, tagRepository.query(new FindAllTagSpecification()).size());
    }
}