package com.epam.esm.specification;

import org.springframework.jdbc.core.PreparedStatementSetter;

/**
 * interface is used to specify data from corresponding Repository
 *
 * @author Dzmitry Platonov on 2019-09-23.
 * @version 0.0.1
 */
public interface SqlSpecification {

    String sql();
    PreparedStatementSetter setStatement();

}
