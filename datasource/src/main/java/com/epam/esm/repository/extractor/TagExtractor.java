package com.epam.esm.repository.extractor;

import com.epam.esm.entity.Tag;
import org.springframework.jdbc.core.ResultSetExtractor;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

/**
 * giftcertificates
 *
 * @author Dzmitry Platonov on 2019-10-02.
 * @version 0.0.1
 */
public class TagExtractor implements ResultSetExtractor<List<Tag>> {

    private String idColumn;
    private String titleColumn;

    public TagExtractor(String idColumn, String titleColumn) {
        this.idColumn = idColumn;
        this.titleColumn = titleColumn;
    }

    @Override
    public List<Tag> extractData(ResultSet resultSet) throws SQLException {
        List<Tag> tags = new LinkedList<>();
        while (resultSet.next()) {
            tags.add(new Tag(resultSet.getLong(idColumn), resultSet.getString(titleColumn)));
        }
        return tags;
    }
}
