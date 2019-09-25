package com.epam.esm.repository;

import com.epam.esm.AppConfig;
import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.entity.Tag;
import com.epam.esm.repository.specification.FindAllCertificatesSpecification;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashSet;

import static org.junit.jupiter.api.Assertions.*;

/**
 * giftcertificates
 *
 * @author Dzmitry Platonov on 2019-09-24.
 * @version 0.0.1
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {AppConfig.class})
public class CertificateRepositoryTest extends DatabaseSetupExtension {

    private static final Logger log = LogManager.getLogger();

    @Autowired
    private Repository<GiftCertificate> certificateRepository;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Test
    public void testAddPositive() {
        certificateRepository.add(new GiftCertificate("flowers", "one hundred roses", new BigDecimal(50),
                LocalDate.of(2019,1,1), LocalDate.of(2019,6,1),
                LocalDate.of(2021, 1,1), new HashSet<>()));
        assertEquals(4, (int) jdbcTemplate.queryForObject("select count(*) from certificate", Integer.class));
    }

    @Test
    public void testQueryPositive() {
        assertEquals(3, certificateRepository.query(new FindAllCertificatesSpecification()).size());
    }
}