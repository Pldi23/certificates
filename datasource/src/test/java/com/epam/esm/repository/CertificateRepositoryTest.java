package com.epam.esm.repository;

import com.epam.esm.DatabaseSetupExtension;
import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.entity.Tag;
import com.epam.esm.repository.extractor.GiftCertificateExtractor;
import com.epam.esm.specification.FindAllCertificatesSpecification;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;
import java.util.Set;

import static org.junit.Assert.assertEquals;


/**
 * gift certificates
 *
 * @author Dzmitry Platonov on 2019-09-24.
 * @version 0.0.1
 */

public class CertificateRepositoryTest extends DatabaseSetupExtension {

    @Autowired
    private Repository<GiftCertificate> certificateRepository;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Test
    public void testAddPositive() {
        certificateRepository.add(new GiftCertificate.Builder()
                .withId(4L)
                .withName("flowers")
                .withDescription("one hundred roses")
                .withPrice(new BigDecimal(50))
                .withCreationDate(LocalDate.of(2019, 1, 1))
                .withModificationDate(LocalDate.of(2019, 6, 1))
                .withExpirationDate(LocalDate.of(2021, 1, 1))
                .withTags(Set.of(new Tag(8L, "yellow")))
                .build());

        assertEquals(4, (int) jdbcTemplate.queryForObject("select count(*) from certificate", Integer.class));
    }

    @Test
    public void testQueryPositive() {
        assertEquals(3, certificateRepository.query(new FindAllCertificatesSpecification()).size());
    }

    @Test
    public void testRemovePositive() {
        certificateRepository.remove(1);
        assertEquals(2, (int) jdbcTemplate.queryForObject("select count(*) from certificate", Integer.class));
    }

    @Test
    public void testUpdate() {

        GiftCertificate giftCertificate = new GiftCertificate.Builder()
                .withId(1L)
                .withName("sport car")
                .withDescription("1 hour lamborghini ride")
                .withPrice(new BigDecimal(300))
                .withCreationDate(LocalDate.of(2019, 1, 1))
                .withModificationDate(LocalDate.of(2019, 6, 1))
                .withExpirationDate(LocalDate.of(2021, 1, 1))
                .withTags(Set.of(new Tag(9L, "blue"), new Tag(1L, "extreme")))
                .build();

        certificateRepository.update(giftCertificate);

        GiftCertificate actual = jdbcTemplate.query(
                "select * from certificate join certificate_tag on certificate.id = certificate_id " +
                        "left join tag on certificate_tag.tag_id = tag.id where certificate_id = ?"
                ,
                preparedStatement -> preparedStatement.setLong(1, giftCertificate.getId()),
                new GiftCertificateExtractor("tag_id")).get(0);

        assertEquals(giftCertificate, actual);
    }

    @Test
    public void testFindOne() {
        GiftCertificate actual = certificateRepository.findOne(1).get();
        GiftCertificate expected = new GiftCertificate.Builder()
                .withId(1L)
                .withName("sport car")
                .withDescription("1 hour lamborghini ride")
                .withPrice(new BigDecimal(250))
                .withCreationDate(LocalDate.of(2019, 1, 1))
                .withModificationDate(LocalDate.of(2019, 1, 6))
                .withExpirationDate(LocalDate.of(2021, 1, 1))
                .withTags(Set.of(new Tag(3L, "luxury"), new Tag(1L, "extreme"), new Tag(5L, "car")))
                .build();
        assertEquals(expected, actual);
    }

    @Test
    public void testFindOneEmpty() {
        assertEquals(Optional.empty(), certificateRepository.findOne(200));
    }
}