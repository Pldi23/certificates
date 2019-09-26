package com.epam.esm.repository;

import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.repository.extractor.GiftCertificateExtractor;
import com.epam.esm.specification.SqlSpecification;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
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

    private static final Logger log = LogManager.getLogger();


    private static final String SQL_INSERT =
            "insert into certificate(id, name, description, price, creationdate, modificationdate, expirationdate) " +
                    "values (?, ?, ?, ?, ?, ?, ?);";

    private static final String SQL_INSERT_LINK = "insert into certificate_tag(certificate_id, tag_id) values (?, ?);";
    private static final String SQL_INSERT_TAG = "insert into tag(id, title) values (?, ?);";
    private static final String SQL_DETECT_TAG = "select count(*) from tag where id = ?;";
    private static final String SQL_DELETE = "delete from certificate where id = ?;";
    private static final String SQL_DELETE_LINK = "delete from certificate_tag where certificate_id = ?;";
    private static final String SQL_UPDATE =
            "update certificate set name = ?, description = ?, price = ?, creationdate = ?, modificationdate = ?," +
                    " expirationdate = ? where id = ?;";


    private JdbcTemplate jdbcTemplate;

    @Autowired
    public CertificateRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void add(GiftCertificate giftCertificate) {
        jdbcTemplate.update(SQL_INSERT,
                giftCertificate.getId(),
                giftCertificate.getName(),
                giftCertificate.getDescription(),
                giftCertificate.getPrice(),
                Date.valueOf(giftCertificate.getCreationDate()),
                Date.valueOf(giftCertificate.getModificationDate()),
                Date.valueOf(giftCertificate.getExpirationDate()));
        giftCertificate.getTags().forEach(tag -> {
            if (jdbcTemplate.queryForObject(SQL_DETECT_TAG, Integer.class, tag.getId()) == 0) {
                jdbcTemplate.update(SQL_INSERT_TAG, tag.getId(), tag.getTitle());
            }
            jdbcTemplate.update(SQL_INSERT_LINK, giftCertificate.getId(), tag.getId());
        });
        log.debug(giftCertificate + " successfully added");
    }

    @Override
    public void remove(GiftCertificate giftCertificate) {
        jdbcTemplate.update(SQL_DELETE_LINK, giftCertificate.getId());
        jdbcTemplate.update(SQL_DELETE, giftCertificate.getId());
        log.debug(giftCertificate + " successfully removed");
    }

    @Override
    public void update(GiftCertificate giftCertificate) {
        jdbcTemplate.update(SQL_UPDATE,
                giftCertificate.getName(),
                giftCertificate.getDescription(),
                giftCertificate.getPrice(),
                Date.valueOf(giftCertificate.getCreationDate()),
                Date.valueOf(giftCertificate.getModificationDate()),
                Date.valueOf(giftCertificate.getExpirationDate()),
                giftCertificate.getId());
        jdbcTemplate.update(SQL_DELETE_LINK, giftCertificate.getId());
        giftCertificate.getTags().forEach(tag -> {
            if (jdbcTemplate.queryForObject(SQL_DETECT_TAG, Integer.class, tag.getId()) == 0) {
                jdbcTemplate.update(SQL_INSERT_TAG, tag.getId(), tag.getTitle());
            }
            jdbcTemplate.update(SQL_INSERT_LINK, giftCertificate.getId(), tag.getId());
        });
        log.debug(giftCertificate + " successfully updated");
    }

    @Override
    public List<GiftCertificate> query(SqlSpecification specification) {
        return jdbcTemplate.query(specification.sql(), specification.setStatement(), new GiftCertificateExtractor());
    }
}
