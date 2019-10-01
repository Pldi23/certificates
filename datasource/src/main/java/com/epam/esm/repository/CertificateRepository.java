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
import java.util.*;

import static com.epam.esm.repository.SqlConstant.*;

@Component(value = "CertificateRepository")
@Transactional
public class CertificateRepository implements AbstractCertificateRepository {

    private JdbcTemplate jdbcTemplate;

    @Autowired
    public CertificateRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void add(GiftCertificate giftCertificate) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(SQL_INSERT_CERTIFICATE, new String[]{"id"});
            ps.setString(1, giftCertificate.getName());
            ps.setString(2, giftCertificate.getDescription());
            ps.setBigDecimal(3, giftCertificate.getPrice());
            ps.setDate(4, Date.valueOf(giftCertificate.getCreationDate()));
            ps.setDate(5, Date.valueOf(giftCertificate.getModificationDate()));
            ps.setDate(6, Date.valueOf(giftCertificate.getExpirationDate()));
            return ps;
        }, keyHolder);
        giftCertificate.getTags().forEach(tag -> {
            if (jdbcTemplate.queryForObject(SQL_CERTIFICATE_DETECT_TAG, Integer.class, tag.getId()) == 0) {
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
    }

    @Override
    public void remove(GiftCertificate giftCertificate) {
        jdbcTemplate.update(SQL_CERTIFICATE_DELETE_LINK, giftCertificate.getId());
        jdbcTemplate.update(SQL_CERTIFICATE_DELETE, giftCertificate.getId());
    }

    @Override
    public void update(GiftCertificate giftCertificate) {

        jdbcTemplate.update(SQL_CERTIFICATE_DELETE_LINK, giftCertificate.getId());
        giftCertificate.getTags().forEach(tag -> {
            if (jdbcTemplate.queryForObject(SQL_CERTIFICATE_DETECT_TAG, Integer.class, tag.getId()) == 0) {
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
        jdbcTemplate.update(SQL_CERTIFICATE_UPDATE,
                giftCertificate.getName(),
                giftCertificate.getDescription(),
                giftCertificate.getPrice(),
                Date.valueOf(giftCertificate.getCreationDate()),
                Date.valueOf(giftCertificate.getModificationDate()),
                Date.valueOf(giftCertificate.getExpirationDate()),
                giftCertificate.getId());
    }

    @Override
    public List<GiftCertificate> query(SqlSpecification specification) {
        return jdbcTemplate.query(specification.sql(), specification.setStatement(),
                new GiftCertificateExtractor(CERTIFICATE_EXTRACTOR_TAG_ID_COLUMN));
    }


    @Override
    public void removeById(long id) {
        jdbcTemplate.update(SQL_CERTIFICATE_DELETE_LINK, id);
        jdbcTemplate.update(SQL_CERTIFICATE_DELETE, id);
    }

    @Override
    public List<GiftCertificate> getCertificatesByTagId(long tagId) {
        return jdbcTemplate.query(
                SQL_SELECT_CERTIFICATES_BY_TAG,
                preparedStatement -> preparedStatement.setLong(1, tagId),
                new GiftCertificateExtractor(CERTIFICATE_EXTRACTOR_OUT_TAG_ID));
    }
}
