package com.epam.esm.specification;

import org.springframework.jdbc.core.PreparedStatementSetter;

/**
 * to select certificate by {@link com.epam.esm.entity.GiftCertificate}'s id from {@link com.epam.esm.repository.CertificateRepository}
 *
 * @author Dzmitry Platonov on 2019-09-26.
 * @version 0.0.1
 */
public class FindCertificateByIdSpecification implements SqlSpecification {

    private long id;

    public FindCertificateByIdSpecification(long id) {
        this.id = id;
    }


    @Override
    public String sql() {
        return SqlSpecificationConstant.SQL_CERTIFICATE_BY_ID_SPECIFICATION;
    }

    @Override
    public PreparedStatementSetter setStatement() {
        return preparedStatement -> preparedStatement.setLong(1, id);
    }
}
