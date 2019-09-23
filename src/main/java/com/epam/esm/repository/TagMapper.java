package com.epam.esm.repository;

import com.epam.esm.entity.Tag;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * giftcertificates
 *
 * @author Dzmitry Platonov on 2019-09-23.
 * @version 0.0.1
 */
public class TagMapper implements RowMapper<Tag> {

    @Override
    public Tag mapRow(ResultSet resultSet, int rowNum) throws SQLException {
        Tag tag = new Tag();
        tag.setTitle(resultSet.getString("title"));
        return tag;
    }
}
