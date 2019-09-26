package com.epam.esm.repository;

import com.epam.esm.entity.Tag;
import com.epam.esm.specification.SqlSpecification;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * gift certificates
 *
 * @author Dzmitry Platonov on 2019-09-23.
 * @version 0.0.1
 */
@Component(value = "TagRepository")
@Transactional
public class TagRepository implements Repository<Tag> {

    private static final Logger log = LogManager.getLogger();


    private static final String SQL_INSERT = "insert into tag (id, title) values (?, ?)";
    private static final String SQL_DELETE = "delete from tag where id = ?;";
    private static final String SQL_DELETE_LINK = "delete from certificate_tag where tag_id = ?;";

    private JdbcTemplate jdbcTemplate;

    @Autowired
    public TagRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void add(Tag tag) {
        jdbcTemplate.update(SQL_INSERT, tag.getId(), tag.getTitle());
        log.debug(tag + " successfully added");
    }

    @Override
    public void remove(Tag tag) {
        jdbcTemplate.update(SQL_DELETE_LINK, tag.getId());
        jdbcTemplate.update(SQL_DELETE, tag.getId());
        log.debug(tag + " successfully removed");
    }

    @Override
    public void update(Tag tag) {
        throw new UnsupportedOperationException();
    }

    @Override
    public List<Tag> query(SqlSpecification specification) {
        return jdbcTemplate.query(specification.sql(), specification.setStatement(), new BeanPropertyRowMapper<>(Tag.class));
    }
}
