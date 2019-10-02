package com.epam.esm.repository;

import com.epam.esm.entity.GiftCertificate;

import com.epam.esm.repository.extractor.GiftCertificateExtractor;
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
public class CertificateRepository implements Repository<GiftCertificate> {

    private JdbcTemplate jdbcTemplate;

    @Autowired
    public CertificateRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Transactional
    @Override
    public Optional<GiftCertificate> add(GiftCertificate giftCertificate) {
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
            if (tag.getId() != null && jdbcTemplate.queryForObject(SQL_CERTIFICATE_DETECT_TAG, Integer.class, tag.getId()) == 0) {
                KeyHolder tagIdHolder = new GeneratedKeyHolder();
                jdbcTemplate.update(connection -> {
                    PreparedStatement ps = connection.prepareStatement(SQL_CERTIFICATE_INSERT_TAG, new String[]{"id"});
                    ps.setString(1, tag.getTitle());
                    return ps;
                }, tagIdHolder);
                jdbcTemplate.update(SQL_CERTIFICATE_INSERT_LINK, keyHolder.getKey(), tagIdHolder.getKey());
            } else {
                jdbcTemplate.update(SQL_CERTIFICATE_INSERT_LINK, keyHolder.getKey(), tag.getId());
            }
        });
        giftCertificate.setId(keyHolder.getKey().longValue());
        return insertionResult != 0 ? Optional.of(giftCertificate) : Optional.empty();
    }

    @Transactional
    @Override
    public boolean update(GiftCertificate giftCertificate) {

        int updateResult = jdbcTemplate.update(SQL_CERTIFICATE_UPDATE,
                giftCertificate.getName(),
                giftCertificate.getDescription(),
                giftCertificate.getPrice(),
                setDate(giftCertificate.getCreationDate()),
                setDate(giftCertificate.getModificationDate()),
                setDate(giftCertificate.getExpirationDate()),
                giftCertificate.getId());
        if (updateResult == 0) {
            return false;
        }
        jdbcTemplate.update(SQL_CERTIFICATE_DELETE_LINK, giftCertificate.getId());
        giftCertificate.getTags().stream().filter(Objects::nonNull).forEach(tag -> {
            if (tag.getId() != null && jdbcTemplate.queryForObject(SQL_CERTIFICATE_DETECT_TAG, Integer.class, tag.getId()) == 0) {
                KeyHolder idHolder = new GeneratedKeyHolder();
                jdbcTemplate.update(connection -> {
                    PreparedStatement ps = connection.prepareStatement(SQL_CERTIFICATE_INSERT_TAG,new String[]{"id"});
                    ps.setString(1, tag.getTitle());
                    return ps;
                }, idHolder);
                jdbcTemplate.update(SQL_CERTIFICATE_INSERT_LINK, giftCertificate.getId(), idHolder.getKey().longValue());
            } else {
                jdbcTemplate.update(SQL_CERTIFICATE_INSERT_LINK, giftCertificate.getId(), tag.getId());
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
    public List<GiftCertificate> query(SqlSpecification<GiftCertificate> specification) {
        return jdbcTemplate.query(specification.sql(), specification.setStatement(), specification.provideExtractor());
    }

    @Transactional
    @Override
    public void remove(long id) {
        jdbcTemplate.update(SQL_CERTIFICATE_DELETE_LINK, id);
        jdbcTemplate.update(SQL_CERTIFICATE_DELETE, id);
    }

    private Date setDate(LocalDate localDate) {
        return localDate != null ? Date.valueOf(localDate) : null;
    }
}
