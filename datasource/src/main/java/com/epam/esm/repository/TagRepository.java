package com.epam.esm.repository;

import com.epam.esm.entity.Tag;
import com.epam.esm.repository.extractor.TagExtractor;
import com.epam.esm.specification.SqlSpecification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.*;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.sql.PreparedStatement;
import java.util.List;
import java.util.Optional;

import static com.epam.esm.repository.SqlConstant.*;

@Component
public class TagRepository implements AbstractTagRepository {

    private JdbcTemplate jdbcTemplate;

    @Autowired
    public TagRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Transactional
    @Override
    public Optional<Tag> save(Tag tag) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        if (jdbcTemplate.query(SQL_DETECT_TAG, ps -> ps.setString(1, tag.getTitle()),
                new TagExtractor(SQL_TAG_ID_COLUMN, SQL_TAG_TITLE_COLUMN)).isEmpty()) {
            int insertionResult = jdbcTemplate.update(connection -> {
                PreparedStatement ps = connection.prepareStatement(SQL_TAG_INSERT, new String[]{"id"});
                ps.setString(1, tag.getTitle());
                return ps;
            }, keyHolder);
            tag.setId(keyHolder.getKey().longValue());
            return insertionResult != 0 ? Optional.of(tag) : Optional.empty();
        }
        return Optional.empty();
    }

    @Override
    public Optional<Tag> findOne(long id) {
        List<Tag> tags = jdbcTemplate.query(SQL_SELECT_TAG_BY_ID,
                ps -> ps.setLong(1, id),
                new TagExtractor(SQL_TAG_ID_COLUMN, SQL_TAG_TITLE_COLUMN));
        return !tags.isEmpty() ? Optional.of(tags.get(0)) : Optional.empty();
    }

    @Override
    public List<Tag> findAll() {
        return jdbcTemplate.query(SQL_SELECT_TAG_ALL, new TagExtractor(SQL_TAG_ID_COLUMN, SQL_TAG_TITLE_COLUMN));
    }

    @Override
    public List<Tag> query(SqlSpecification<Tag> specification) {
        return jdbcTemplate.query(specification.sql(), specification.setStatement(), specification.provideExtractor());
    }

    @Transactional
    @Override
    public boolean remove(long id) {
        jdbcTemplate.update(SQL_TAG_DELETE_LINK, id);
        int updatedRows = jdbcTemplate.update(SQL_TAG_DELETE, id);
        return updatedRows == 1;
    }
}
