package com.epam.esm.repository.mapper;

import com.epam.esm.entity.Tag;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * gift certificates
 *
 * @author Dzmitry Platonov on 2019-09-23.
 * @version 0.0.1
 */
public class TagMapper implements RowMapper<Tag> {

    private static final String TITLE_COLUMN = "title";

    @Override
    public Tag mapRow(ResultSet resultSet, int rowNum) throws SQLException {
        Tag tag = new Tag();
        tag.setTitle(resultSet.getString(TITLE_COLUMN));
        return tag;
    }
}
