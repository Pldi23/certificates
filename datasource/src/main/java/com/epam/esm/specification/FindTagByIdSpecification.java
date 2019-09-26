package com.epam.esm.specification;

import org.springframework.jdbc.core.PreparedStatementSetter;

/**
 * giftcertificates
 *
 * @author Dzmitry Platonov on 2019-09-26.
 * @version 0.0.1
 */
public class FindTagByIdSpecification implements SqlSpecification {

    private static final String SQL_SPECIFICATION = "select * from tag where id = ?";

    private long id;

    public FindTagByIdSpecification(long id) {
        this.id = id;
    }

    @Override
    public String sql() {
        return SQL_SPECIFICATION;
    }

    @Override
    public PreparedStatementSetter setStatement() {
        return preparedStatement -> preparedStatement.setLong(1, id);
    }
}
