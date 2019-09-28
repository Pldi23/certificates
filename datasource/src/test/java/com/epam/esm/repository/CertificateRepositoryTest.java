package com.epam.esm.repository;

import com.epam.esm.DataSourceConfig;
import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.entity.Tag;
import com.epam.esm.repository.extractor.GiftCertificateExtractor;
import com.epam.esm.specification.FindAllCertificatesSpecification;
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
import java.util.Set;

import static org.junit.Assert.assertEquals;


/**
 * giftcertificates
 *
 * @author Dzmitry Platonov on 2019-09-24.
 * @version 0.0.1
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {DataSourceConfig.class})
public class CertificateRepositoryTest extends DatabaseSetupExtension {

    private static final Logger log = LogManager.getLogger();

    @Autowired
//    @Qualifier("CertificateRepository")
    private AbstractCertificateRepository certificateRepository;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Test
    public void testAddPositive() {
        certificateRepository.add(new GiftCertificate(4,"flowers", "one hundred roses", new BigDecimal(50),
                LocalDate.of(2019,1,1), LocalDate.of(2019,6,1),
                LocalDate.of(2021, 1,1), Set.of(new Tag(8, "yellow"))));
        assertEquals(4, (int) jdbcTemplate.queryForObject("select count(*) from certificate", Integer.class));
    }

    @Test
    public void testQueryPositive() {
        assertEquals(3, certificateRepository.query(new FindAllCertificatesSpecification()).size());
    }

    @Test
    public void testRemovePositive() {
        certificateRepository.remove(new GiftCertificate(1,"sport car", "1 hour lamborghini ride",
                new BigDecimal(250), LocalDate.of(2019,1,1),
                LocalDate.of(2019,6,1),
                LocalDate.of(2021, 1,1), new HashSet<>()));
        assertEquals(2, (int) jdbcTemplate.queryForObject("select count(*) from certificate", Integer.class));
    }

    @Test
    public void testUpdate() {

        GiftCertificate giftCertificate = new GiftCertificate(1,"sport car", "1 hour lamborghini ride",
                new BigDecimal(300), LocalDate.of(2019,1,1),
                LocalDate.of(2019,6,1),
                LocalDate.of(2021, 1,1), Set.of(new Tag(9, "blue"), new Tag(10, "red")));

        certificateRepository.update(giftCertificate);

        GiftCertificate actual = jdbcTemplate.query(
                "select * from certificate join certificate_tag on certificate.id = certificate_id " +
                        "left join tag on certificate_tag.tag_id = tag.id where certificate_id = ?"
                        ,
                preparedStatement -> preparedStatement.setLong(1, giftCertificate.getId()),
                new GiftCertificateExtractor()).get(0);

        assertEquals(giftCertificate, actual);
    }
}