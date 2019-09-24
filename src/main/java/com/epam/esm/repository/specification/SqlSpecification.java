package com.epam.esm.repository.specification;

import org.springframework.jdbc.core.PreparedStatementSetter;

/**
 * giftcertificates
 *
 * @author Dzmitry Platonov on 2019-09-23.
 * @version 0.0.1
 */
public interface SqlSpecification {

    String sqlClause();
    PreparedStatementSetter prepareStatement();

}
