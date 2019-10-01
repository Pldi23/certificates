package com.epam.esm.specification;

import org.springframework.jdbc.core.PreparedStatementSetter;

/**
 * to select tag by id
 *
 * @author Dzmitry Platonov on 2019-09-26.
 * @version 0.0.1
 */
public class FindTagByIdSpecification implements SqlSpecification {

    private long id;

    public FindTagByIdSpecification(long id) {
        this.id = id;
    }

    @Override
    public String sql() {
        return SqlSpecificationConstant.SQL_TAG_BY_ID_SPECIFICATION;
    }

    @Override
    public PreparedStatementSetter setStatement() {
        return preparedStatement -> preparedStatement.setLong(1, id);
    }
}
