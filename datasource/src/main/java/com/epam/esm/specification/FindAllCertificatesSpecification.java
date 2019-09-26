package com.epam.esm.specification;

import org.springframework.jdbc.core.PreparedStatementSetter;

/**
 * giftcertificates
 *
 * @author Dzmitry Platonov on 2019-09-25.
 * @version 0.0.1
 */
public class FindAllCertificatesSpecification implements SqlSpecification {

    private static final String SQL_SPECIFICATION =
            "select * from certificate join certificate_tag on certificate.id = certificate_id " +
                    "left join tag on certificate_tag.tag_id = tag.id";

    @Override
    public String sql() {
        return SQL_SPECIFICATION;
    }

    @Override
    public PreparedStatementSetter setStatement() {
        return null;
    }
}
