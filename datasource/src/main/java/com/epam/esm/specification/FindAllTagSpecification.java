package com.epam.esm.specification;

import org.springframework.jdbc.core.PreparedStatementSetter;

/**
 * gift certificates
 *
 * @author Dzmitry Platonov on 2019-09-24.
 * @version 0.0.1
 */
public class FindAllTagSpecification implements SqlSpecification {

    @Override
    public String sql() {
        return SqlSpecificationConstant.SQL_TAG_ALL_SPECIFICATION;
    }

    @Override
    public PreparedStatementSetter setStatement() {
        return null;
    }
}