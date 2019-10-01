package com.epam.esm.repository.mapper;

import com.epam.esm.entity.Tag;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * gift certificates
 *
 * @author Dzmitry Platonov on 2019-09-29.
 * @version 0.0.1
 */
public class TagMapper implements RowMapper<Tag> {

    private String idColumn;
    private String titleColumn;

    public TagMapper(String idColumn, String titleColumn) {
        this.idColumn = idColumn;
        this.titleColumn = titleColumn;
    }

    @Override
    public Tag mapRow(ResultSet resultSet, int i) throws SQLException {
        return new Tag(resultSet.getLong(idColumn), resultSet.getString(titleColumn));
    }

    //"out_id" "out_title"
}
