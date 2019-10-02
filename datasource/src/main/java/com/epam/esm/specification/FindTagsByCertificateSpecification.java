package com.epam.esm.specification;

import com.epam.esm.entity.Tag;
import com.epam.esm.repository.extractor.TagExtractor;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.jdbc.core.ResultSetExtractor;

import java.util.List;

/**
 * giftcertificates
 *
 * @author Dzmitry Platonov on 2019-10-02.
 * @version 0.0.1
 */
public class FindTagsByCertificateSpecification implements SqlSpecification<Tag>{

    private long id;

    public FindTagsByCertificateSpecification(long id) {
        this.id = id;
    }

    @Override
    public String sql() {
        return SqlSpecificationConstant.SQL_GET_TAGS_BY_CERTIFICATE_FUNCTION;
    }

    @Override
    public PreparedStatementSetter setStatement() {
        return preparedStatement ->
                preparedStatement.setLong(1, id);
    }

    @Override
    public ResultSetExtractor<List<Tag>> provideExtractor() {
        return new TagExtractor(SqlSpecificationConstant.SQL_TAG_ID_FUNCTION_COLUMN,
                SqlSpecificationConstant.SQL_TAG_TITLE_FUNCTION_COLUMN);
    }
}
