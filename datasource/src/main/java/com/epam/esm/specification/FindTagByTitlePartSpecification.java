package com.epam.esm.specification;

import com.epam.esm.entity.Tag;
import com.epam.esm.repository.extractor.TagExtractor;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.jdbc.core.ResultSetExtractor;

import java.util.List;

/**
 * to select tag by title or a part
 *
 * @author Dzmitry Platonov on 2019-09-24.
 * @version 0.0.1
 */
public class FindTagByTitlePartSpecification implements SqlSpecification<Tag> {

    private String stringPart;

    public FindTagByTitlePartSpecification(String stringPart) {
        this.stringPart = stringPart;
    }

    @Override
    public String sql() {
        return SqlSpecificationConstant.SQL_TAG_BY_TITLE_SPECIFICATION;
    }

    @Override
    public PreparedStatementSetter setStatement() {
        return preparedStatement -> preparedStatement.setString(1, "%" + stringPart + "%");
    }

    @Override
    public ResultSetExtractor<List<Tag>> provideExtractor() {
        return new TagExtractor(SqlSpecificationConstant.SQL_TAG_ID_COLUMN, SqlSpecificationConstant.SQL_TAG_TITLE_COLUMN);
    }

}
