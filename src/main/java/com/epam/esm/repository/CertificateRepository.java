package com.epam.esm.repository;

import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.repository.mapper.CertificateMapper;
import com.epam.esm.repository.specification.SqlSpecification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * giftcertificates
 *
 * @author Dzmitry Platonov on 2019-09-24.
 * @version 0.0.1
 */
@Component
@Transactional
public class CertificateRepository implements Repository<GiftCertificate> {

    private static final String SQL_INSERT =
                    "insert into certificate(name, description, price, creationdate, modificationdate, expirationdate) " +
                    "values (?, ?, ?, ?, ?, ?);";


    private JdbcTemplate jdbcTemplate;

    @Autowired
    public CertificateRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void add(GiftCertificate giftCertificate) {
        jdbcTemplate.update(SQL_INSERT, giftCertificate.getName(), giftCertificate.getDescription(),
                giftCertificate.getPrice(), giftCertificate.getCreationDate(), giftCertificate.getModificationDate(),
                giftCertificate.getExpirationDate());
    }

    @Override
    public void remove(GiftCertificate giftCertificate) {

    }

    @Override
    public void update(GiftCertificate giftCertificate) {

    }

    @Override
    public List<GiftCertificate> query(SqlSpecification specification) {
        return jdbcTemplate.query(specification.sqlClause(), specification.prepareStatement(), new CertificateMapper());
    }
}
