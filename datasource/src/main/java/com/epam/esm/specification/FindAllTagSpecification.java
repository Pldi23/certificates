package com.epam.esm.specification;

import com.epam.esm.entity.Tag;
import com.epam.esm.repository.extractor.TagExtractor;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.jdbc.core.ResultSetExtractor;

import java.util.List;

/**
 * to select all tags from {@link com.epam.esm.repository.TagRepository}
 *
 * @author Dzmitry Platonov on 2019-09-24.
 * @version 0.0.1
 */
public class FindAllTagSpecification implements SqlSpecification<Tag> {

    @Override
    public String sql() {
        return SqlSpecificationConstant.SQL_TAG_ALL_SPECIFICATION;
    }

    @Override
    public PreparedStatementSetter setStatement() {
        return null;
    }

    @Override
    public ResultSetExtractor<List<Tag>> provideExtractor() {
        return new TagExtractor(SqlSpecificationConstant.SQL_TAG_ID_COLUMN, SqlSpecificationConstant.SQL_TAG_TITLE_COLUMN);
    }
}
