package com.epam.esm.specification;

import org.springframework.jdbc.core.PreparedStatementSetter;

/**
 * to select tag by title or a part
 *
 * @author Dzmitry Platonov on 2019-09-24.
 * @version 0.0.1
 */
public class FindTagByTitlePartSpecification implements SqlSpecification {

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
}
