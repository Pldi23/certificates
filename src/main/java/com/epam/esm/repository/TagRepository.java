package com.epam.esm.repository;

import com.epam.esm.entity.Tag;
import com.epam.esm.repository.specification.SqlSpecification;
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
@Component
@Transactional
public class TagRepository implements Repository<Tag> {

    private static final String SQL_INSERT = "insert into tag (title) values (?)";
    private static final String SQL_DELETE = "delete from tag where title = ?;";
    private static final String SQL_DELETE_LINK = "delete from certificate_tag where tag_title = ?;";

    private JdbcTemplate jdbcTemplate;

    @Autowired
    public TagRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void add(Tag tag) {
        jdbcTemplate.update(SQL_INSERT, tag.getTitle());
    }

    @Override
    public void remove(Tag tag) {
        jdbcTemplate.update(SQL_DELETE_LINK, tag.getTitle());
        jdbcTemplate.update(SQL_DELETE, tag.getTitle());
    }

    @Override
    public void update(Tag tag) {
        throw new UnsupportedOperationException();
    }

    @Override
    public List<Tag> query(SqlSpecification specification) {
        return jdbcTemplate.query(specification.sqlClause(), specification.prepareStatement(), new BeanPropertyRowMapper<>(Tag.class));
    }
}
