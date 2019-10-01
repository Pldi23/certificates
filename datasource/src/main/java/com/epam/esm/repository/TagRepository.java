package com.epam.esm.repository;

import com.epam.esm.entity.Tag;
import com.epam.esm.repository.mapper.TagMapper;
import com.epam.esm.specification.SqlSpecification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.*;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.sql.PreparedStatement;
import java.util.List;

import static com.epam.esm.repository.SqlConstant.*;

@Component(value = "TagRepository")
@Transactional
public class TagRepository implements AbstractTagRepository {

    private JdbcTemplate jdbcTemplate;

    @Autowired
    public TagRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Tag add(Tag tag) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        int insertionResult = jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(SQL_TAG_INSERT, new String[]{"id"});
            ps.setString(1, tag.getTitle());
            return ps;
        }, keyHolder);
        tag.setId(keyHolder.getKey().longValue());
        return insertionResult != 0 ? tag : null;
    }

    @Override
    public void remove(Tag tag) {
        jdbcTemplate.update(SQL_TAG_DELETE_LINK, tag.getId());
        jdbcTemplate.update(SQL_TAG_DELETE, tag.getId());

    }

    @Override
    public void update(Tag tag) {
        throw new UnsupportedOperationException();
    }

    @Override
    public List<Tag> query(SqlSpecification specification) {
        return jdbcTemplate.query(specification.sql(), specification.setStatement(),
                new TagMapper(SQL_TAG_ID_COLUMN, SQL_TAG_TITLE_COLUMN));
    }

    @Override
    public void removeById(long id) {
        jdbcTemplate.update(SQL_TAG_DELETE_LINK, id);
        jdbcTemplate.update(SQL_TAG_DELETE, id);
    }

    @Override
    public List<Tag> getTagsByCertificate(long id) {

        return jdbcTemplate.query(
                SQL_GET_TAGS_BY_CERTIFICATE_FUNCTION,
                preparedStatement ->
                        preparedStatement.setLong(1, id),
                new TagMapper(SQL_TAG_ID_FUNCTION_COLUMN, SQL_TAG_TITLE_FUNCTION_COLUMN));
    }
}
