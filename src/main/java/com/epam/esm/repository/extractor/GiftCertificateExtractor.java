package com.epam.esm.repository.extractor;

import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.entity.Tag;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

/**
 * giftcertificates
 *
 * @author Dzmitry Platonov on 2019-09-25.
 * @version 0.0.1
 */
public class GiftCertificateExtractor implements ResultSetExtractor<List<GiftCertificate>> {

    private static final String NAME_COLUMN = "name";
    private static final String DESCRIPTION_COLUMN = "description";
    private static final String PRICE_COLUMN = "price";
    private static final String CREATION_DATE_COLUMN = "creationdate";
    private static final String MODIFICATION_DATE_COLUMN = "modificationdate";
    private static final String EXPIRATION_DATE_COLUMN = "expirationdate";
    private static final String TAG_TITLE_COLUMN = "tag_title";

    @Override
    public List<GiftCertificate> extractData(ResultSet resultSet) throws SQLException, DataAccessException {
        List<GiftCertificate> giftCertificates = new LinkedList<>();
        Map<String, GiftCertificate> table = new LinkedHashMap<>();
        while (resultSet.next()) {
            if (!table.containsKey(resultSet.getString(NAME_COLUMN))) {

                GiftCertificate giftCertificate = new GiftCertificate();
                giftCertificate.setName(resultSet.getString(NAME_COLUMN));
                giftCertificate.setDescription(resultSet.getString(DESCRIPTION_COLUMN));
                giftCertificate.setPrice(resultSet.getBigDecimal(PRICE_COLUMN));
                giftCertificate.setCreationDate(resultSet.getDate(CREATION_DATE_COLUMN).toLocalDate());
                giftCertificate.setModificationDate(resultSet.getDate(MODIFICATION_DATE_COLUMN).toLocalDate());
                giftCertificate.setExpirationDate(resultSet.getDate(EXPIRATION_DATE_COLUMN).toLocalDate());
                giftCertificate.setTags(new HashSet<>());
                giftCertificate.getTags().add(new Tag(resultSet.getString(TAG_TITLE_COLUMN)));
                table.put(giftCertificate.getName(), giftCertificate);
            } else {
                table.get(resultSet.getString(NAME_COLUMN)).getTags().add(new Tag(resultSet.getString(TAG_TITLE_COLUMN)));
            }
        }
        table.forEach((name, giftCertificate) -> giftCertificates.add(giftCertificate));
        return giftCertificates;
    }
}
