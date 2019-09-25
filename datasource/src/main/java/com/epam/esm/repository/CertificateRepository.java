package com.epam.esm.repository;

import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.repository.extractor.GiftCertificateExtractor;
import com.epam.esm.specification.SqlSpecification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Date;
import java.util.List;

/**
 * gift certificates
 *
 * @author Dzmitry Platonov on 2019-09-24.
 * @version 0.0.1
 */
@Component(value = "CertificateRepository")
@Transactional
public class CertificateRepository implements Repository<GiftCertificate> {

    private static final String SQL_INSERT =
            "insert into certificate(name, description, price, creationdate, modificationdate, expirationdate) " +
                    "values (?, ?, ?, ?, ?, ?);";

    private static final String SQL_INSERT_LINK = "insert into certificate_tag(certificate_name, tag_title) values (?, ?);";
    private static final String SQL_INSERT_TAG = "insert into tag(title) values (?);";
    private static final String SQL_DETECT_TAG = "select count(*) from tag where title = ?;";
    private static final String SQL_DELETE = "delete from certificate where name = ?;";
    private static final String SQL_DELETE_LINK = "delete from certificate_tag where certificate_name = ?;";
    private static final String SQL_UPDATE =
            "update certificate set description = ?, price = ?, creationdate = ?, modificationdate = ?," +
                    " expirationdate = ? where name = ?;";


    private JdbcTemplate jdbcTemplate;

    @Autowired
    public CertificateRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void add(GiftCertificate giftCertificate) {
        jdbcTemplate.update(SQL_INSERT,
                giftCertificate.getName(),
                giftCertificate.getDescription(),
                giftCertificate.getPrice(),
                Date.valueOf(giftCertificate.getCreationDate()),
                Date.valueOf(giftCertificate.getModificationDate()),
                Date.valueOf(giftCertificate.getExpirationDate()));
        giftCertificate.getTags().forEach(tag -> {
            if (jdbcTemplate.queryForObject(SQL_DETECT_TAG, Integer.class, tag.getTitle()) == 0) {
                jdbcTemplate.update(SQL_INSERT_TAG, tag.getTitle());
            }
            jdbcTemplate.update(SQL_INSERT_LINK, giftCertificate.getName(), tag.getTitle());
        });
    }

    @Override
    public void remove(GiftCertificate giftCertificate) {
        jdbcTemplate.update(SQL_DELETE_LINK, giftCertificate.getName());
        jdbcTemplate.update(SQL_DELETE, giftCertificate.getName());
    }

    @Override
    public void update(GiftCertificate giftCertificate) {
        jdbcTemplate.update(SQL_DELETE_LINK, giftCertificate.getName());
        giftCertificate.getTags().forEach(tag -> {
            if (jdbcTemplate.queryForObject(SQL_DETECT_TAG, Integer.class, tag.getTitle()) == 0) {
                jdbcTemplate.update(SQL_INSERT_TAG, tag.getTitle());
            }
            jdbcTemplate.update(SQL_INSERT_LINK, giftCertificate.getName(), tag.getTitle());
        });
        jdbcTemplate.update(SQL_UPDATE,
                giftCertificate.getDescription(),
                giftCertificate.getPrice(),
                Date.valueOf(giftCertificate.getCreationDate()),
                Date.valueOf(giftCertificate.getModificationDate()),
                Date.valueOf(giftCertificate.getExpirationDate()),
                giftCertificate.getName());
    }

    @Override
    public List<GiftCertificate> query(SqlSpecification specification) {
        return jdbcTemplate.query(specification.sql(), specification.setStatement(), new GiftCertificateExtractor());
    }
}
