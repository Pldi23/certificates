package com.epam.esm.specification;

import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.repository.extractor.GiftCertificateExtractor;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.jdbc.core.ResultSetExtractor;

import java.util.List;

import static com.epam.esm.specification.SqlSpecificationConstant.CERTIFICATE_EXTRACTOR_TAG_ID_COLUMN;

/**
 * to select all certificates from {@link com.epam.esm.repository.CertificateRepository}
 *
 * @author Dzmitry Platonov on 2019-09-25.
 * @version 0.0.1
 */
public class FindAllCertificatesSpecification implements SqlSpecification<GiftCertificate> {

    @Override
    public String sql() {
        return SqlSpecificationConstant.SQL_CERTIFICATES_ALL_SPECIFICATION;
    }

    @Override
    public PreparedStatementSetter setStatement() {
        return null;
    }

    @Override
    public ResultSetExtractor<List<GiftCertificate>> provideExtractor() {
        return new GiftCertificateExtractor(CERTIFICATE_EXTRACTOR_TAG_ID_COLUMN);

    }
}
