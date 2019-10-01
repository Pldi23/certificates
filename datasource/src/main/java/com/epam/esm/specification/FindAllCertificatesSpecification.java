package com.epam.esm.specification;

import org.springframework.jdbc.core.PreparedStatementSetter;

/**
 * to select all certificates from {@link com.epam.esm.repository.CertificateRepository}
 *
 * @author Dzmitry Platonov on 2019-09-25.
 * @version 0.0.1
 */
public class FindAllCertificatesSpecification implements SqlSpecification {

    @Override
    public String sql() {
        return SqlSpecificationConstant.SQL_CERTIFICATES_ALL_SPECIFICATION;
    }

    @Override
    public PreparedStatementSetter setStatement() {
        return null;
    }
}
