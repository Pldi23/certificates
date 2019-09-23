package com.epam.esm.repository;

import com.epam.esm.entity.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * giftcertificates
 *
 * @author Dzmitry Platonov on 2019-09-23.
 * @version 0.0.1
 */
@Component
public class TagRepository implements Repository<Tag> {

    private static final String SQL_INSERT = "insert into gift_certificates.public.tag (title) values (?)";

    private JdbcTemplate jdbcTemplate;

    @Autowired
    public TagRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void add(Tag tag) {
        jdbcTemplate.update(SQL_INSERT, tag.getTitle());
    }

    @Override
    public void remove(Tag entity) {

    }

    @Override
    public void update(Tag entity) {

    }

    @Override
    public List<Tag> query(SqlSpecification specification) {
        return null;
    }
}
