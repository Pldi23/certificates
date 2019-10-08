package com.epam.esm.repository;

import com.epam.esm.entity.GiftCertificate;

import com.epam.esm.entity.Tag;
import com.epam.esm.repository.extractor.GiftCertificateExtractor;
import com.epam.esm.repository.extractor.TagExtractor;
import com.epam.esm.specification.SqlSpecification;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.jdbc.core.JdbcTemplate;

import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.time.LocalDate;
import java.util.*;

import static com.epam.esm.repository.SqlConstant.*;

@Component
public class CertificateRepository implements AbstractCertificateRepository {

    private JdbcTemplate jdbcTemplate;

    @Autowired
    public CertificateRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Transactional
    @Override
    public Optional<GiftCertificate> save(GiftCertificate giftCertificate) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        int insertionResult = jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(SQL_INSERT_CERTIFICATE, new String[]{"id"});
            ps.setString(1, giftCertificate.getName());
            ps.setString(2, giftCertificate.getDescription());
            ps.setBigDecimal(3, giftCertificate.getPrice());
            ps.setDate(4, setDate(giftCertificate.getCreationDate()));
            ps.setDate(5, setDate(giftCertificate.getModificationDate()));
            ps.setDate(6, setDate(giftCertificate.getExpirationDate()));
            return ps;
        }, keyHolder);
        giftCertificate.getTags().stream().filter(Objects::nonNull).forEach(tag -> {
            List<Tag> resultTags = jdbcTemplate.query(SQL_CERTIFICATE_DETECT_TAG, ps -> ps.setString(1, tag.getTitle()),
                    new TagExtractor(SQL_TAG_ID_COLUMN, SQL_TAG_TITLE_COLUMN));
            if (resultTags.isEmpty()) {
                KeyHolder tagIdHolder = new GeneratedKeyHolder();
                jdbcTemplate.update(connection -> {
                    PreparedStatement ps = connection.prepareStatement(SQL_CERTIFICATE_INSERT_TAG, new String[]{"id"});
                    ps.setString(1, tag.getTitle());
                    return ps;
                }, tagIdHolder);
                jdbcTemplate.update(SQL_CERTIFICATE_INSERT_LINK, keyHolder.getKey(), tagIdHolder.getKey());
            } else {
                jdbcTemplate.update(SQL_CERTIFICATE_INSERT_LINK, keyHolder.getKey(), resultTags.get(0).getId());
            }
        });
        GiftCertificate resultGiftCertificate = jdbcTemplate.query(SQL_SELECT_CERTIFICATE_BY_ID,
                ps -> ps.setLong(1, keyHolder.getKey().longValue()),
                new GiftCertificateExtractor(CERTIFICATE_EXTRACTOR_TAG_ID_COLUMN)).get(0);
        return insertionResult != 0 ? Optional.of(resultGiftCertificate) : Optional.empty();
    }

    @Transactional
    @Override
    public boolean update(GiftCertificate giftCertificate) {

        int updateResult = jdbcTemplate.update(SQL_CERTIFICATE_UPDATE,
                giftCertificate.getName(),
                giftCertificate.getDescription(),
                giftCertificate.getPrice(),
                setDate(giftCertificate.getModificationDate()),
                setDate(giftCertificate.getExpirationDate()),
                giftCertificate.getId());
        if (updateResult == 0) {
            return false;
        }
        jdbcTemplate.update(SQL_CERTIFICATE_DELETE_LINK, giftCertificate.getId());
        giftCertificate.getTags().stream().filter(Objects::nonNull).forEach(tag -> {
            List<Tag> resultTags = jdbcTemplate.query(SQL_CERTIFICATE_DETECT_TAG, ps -> ps.setString(1, tag.getTitle()),
                    new TagExtractor(SQL_TAG_ID_COLUMN, SQL_TAG_TITLE_COLUMN));
            if (resultTags.isEmpty()) {
                KeyHolder idHolder = new GeneratedKeyHolder();
                jdbcTemplate.update(connection -> {
                    PreparedStatement ps = connection.prepareStatement(SQL_CERTIFICATE_INSERT_TAG, new String[]{"id"});
                    ps.setString(1, tag.getTitle());
                    return ps;
                }, idHolder);
                jdbcTemplate.update(SQL_CERTIFICATE_INSERT_LINK, giftCertificate.getId(), idHolder.getKey().longValue());
            } else {
                jdbcTemplate.update(SQL_CERTIFICATE_INSERT_LINK, giftCertificate.getId(), resultTags.get(0).getId());
            }
        });

        return true;
    }

    @Override
    public Optional<GiftCertificate> findOne(long id) {
        List<GiftCertificate> certificates = jdbcTemplate.query(SQL_SELECT_CERTIFICATE_BY_ID,
                ps -> ps.setLong(1, id),
                new GiftCertificateExtractor(CERTIFICATE_EXTRACTOR_TAG_ID_COLUMN));
        return !certificates.isEmpty() ? Optional.of(certificates.get(0)) : Optional.empty();
    }

    @Override
    public List<GiftCertificate> findAll() {
        return jdbcTemplate.query(SQL_SELECT_ALL_CERTIFICATES, new GiftCertificateExtractor(CERTIFICATE_EXTRACTOR_TAG_ID_COLUMN));
    }

    @Override
    public List<GiftCertificate> query(SqlSpecification<GiftCertificate> specification) {
        return jdbcTemplate.query(specification.sql(), specification.setStatement(), specification.provideExtractor());
    }

    @Transactional
    @Override
    public boolean remove(long id) {
        jdbcTemplate.update(SQL_CERTIFICATE_DELETE_LINK, id);
        int deletedRows = jdbcTemplate.update(SQL_CERTIFICATE_DELETE, id);
        return deletedRows == 1;
    }

    private Date setDate(LocalDate localDate) {
        return localDate != null ? Date.valueOf(localDate) : null;
    }
}
