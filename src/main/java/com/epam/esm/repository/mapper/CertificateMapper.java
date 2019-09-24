package com.epam.esm.repository.mapper;

import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.entity.Tag;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;

/**
 * gift certificates
 *
 * @author Dzmitry Platonov on 2019-09-24.
 * @version 0.0.1
 */
public class CertificateMapper implements RowMapper<GiftCertificate> {

    private static final String NAME_COLUMN = "name";
    private static final String DESCRIPTION_COLUMN = "description";
    private static final String PRICE_COLUMN = "price";
    private static final String CREATION_DATE_COLUMN = "creationdate";
    private static final String MODIFICATION_DATE_COLUMN = "modificationdate";
    private static final String EXPIRATION_DATE_COLUMN = "expirationdate";

    @Override
    public GiftCertificate mapRow(ResultSet resultSet, int i) throws SQLException {
        Tag tag = new BeanPropertyRowMapper<>(Tag.class).mapRow(resultSet, i);
        GiftCertificate giftCertificate = new BeanPropertyRowMapper<>(GiftCertificate.class).mapRow(resultSet, i);
        giftCertificate.setTags(new HashSet<>());
        giftCertificate.getTags().add(tag);

//        GiftCertificate giftCertificate = new GiftCertificate();
//        giftCertificate.setName(resultSet.getString(NAME_COLUMN));
//        giftCertificate.setDescription(resultSet.getString(DESCRIPTION_COLUMN));
//        giftCertificate.setPrice(resultSet.getBigDecimal(PRICE_COLUMN));
//        giftCertificate.setCreationDate(resultSet.getDate(CREATION_DATE_COLUMN).toLocalDate());
//        giftCertificate.setModificationDate(resultSet.getDate(MODIFICATION_DATE_COLUMN).toLocalDate());
//        giftCertificate.setExpirationDate(resultSet.getDate(EXPIRATION_DATE_COLUMN).toLocalDate());
//        giftCertificate.setTags(new HashSet<>());
        return giftCertificate;
    }
}
