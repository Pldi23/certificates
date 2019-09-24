package com.epam.esm.repository.specification;

import org.springframework.jdbc.core.PreparedStatementSetter;

/**
 * gift certificates
 *
 * @author Dzmitry Platonov on 2019-09-24.
 * @version 0.0.1
 */
public class FindByTitlePartSpecification implements SqlSpecification {

    private static final String SQL_SPECIFICATION = "select * from tag where title = ?";

    private String stringPart;

    public FindByTitlePartSpecification(String stringPart) {
        this.stringPart = stringPart;
    }

    @Override
    public String sqlClause() {
        return SQL_SPECIFICATION;
    }

    @Override
    public PreparedStatementSetter prepareStatement() {
        return preparedStatement -> preparedStatement.setString(1, stringPart);
    }
}
