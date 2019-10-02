package com.epam.esm.specification;

import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.repository.extractor.GiftCertificateExtractor;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.jdbc.core.ResultSetExtractor;

import java.util.List;

/**
 * giftcertificates
 *
 * @author Dzmitry Platonov on 2019-10-02.
 * @version 0.0.1
 */
public class FindCertificatesByTagSpecification implements SqlSpecification<GiftCertificate> {

    private long id;

    public FindCertificatesByTagSpecification(long id) {
        this.id = id;
    }

    @Override
    public String sql() {
        return SqlSpecificationConstant.SQL_SELECT_CERTIFICATES_BY_TAG;
    }

    @Override
    public PreparedStatementSetter setStatement() {
        return preparedStatement -> preparedStatement.setLong(1, id);
    }

    @Override
    public ResultSetExtractor<List<GiftCertificate>> provideExtractor() {
        return new GiftCertificateExtractor(SqlSpecificationConstant.CERTIFICATE_EXTRACTOR_OUT_TAG_ID);
    }
}
