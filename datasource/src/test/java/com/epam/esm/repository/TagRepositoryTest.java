package com.epam.esm.repository;

import com.epam.esm.config.DataSourceConfig;
import com.epam.esm.DatabaseSetupExtension;
import com.epam.esm.entity.Tag;
import com.epam.esm.specification.FindAllTagSpecification;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.junit.Assert.assertEquals;

/**
 * gift certificates
 *
 * @author Dzmitry Platonov on 2019-09-23.
 * @version 0.0.1
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {DataSourceConfig.class})
public class TagRepositoryTest extends DatabaseSetupExtension {

    @Autowired
    @Qualifier("TagRepository")
    private AbstractTagRepository tagRepository;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Test
    public void testAddPositive() {
        tagRepository.add(new Tag(9, "test"));
        assertEquals(8, (int) jdbcTemplate.queryForObject("select count(*) from tag", Integer.class));
    }

    @Test
    public void testRemovePositive() {
        tagRepository.remove(new Tag(7,"jump"));
        assertEquals(6, (int) jdbcTemplate.queryForObject("select count(*) from tag", Integer.class));
    }

    @Test
    public void testQuery() {
        assertEquals(7, tagRepository.query(new FindAllTagSpecification()).size());
    }

}