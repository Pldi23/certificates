package com.epam.esm.specification;

import org.springframework.jdbc.core.PreparedStatementSetter;

/**
 * gift certificates
 *
 * @author Dzmitry Platonov on 2019-09-24.
 * @version 0.0.1
 */
public class FindAllTagSpecification implements SqlSpecification {

    private static final String SQL_SPECIFICATION = "select * from tag";

    @Override
    public String sql() {
        return SQL_SPECIFICATION;
    }

    @Override
    public PreparedStatementSetter setStatement() {
        return null;
    }
}
