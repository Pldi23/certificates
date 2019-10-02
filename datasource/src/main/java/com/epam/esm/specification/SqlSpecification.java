package com.epam.esm.specification;

import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.jdbc.core.ResultSetExtractor;

import java.util.List;

/**
 * interface is used to specify data from corresponding Repository
 *
 * @author Dzmitry Platonov on 2019-09-23.
 * @version 0.0.1
 */
public interface SqlSpecification<T> {

    String sql();
    PreparedStatementSetter setStatement();
    ResultSetExtractor<List<T>> provideExtractor();

}
