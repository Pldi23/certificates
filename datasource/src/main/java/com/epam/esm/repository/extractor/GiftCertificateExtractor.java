package com.epam.esm.repository.extractor;

import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.entity.Tag;
import org.springframework.jdbc.core.ResultSetExtractor;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * extractor for {@link GiftCertificate} and nested {@link Tag}
 *
 * @author Dzmitry Platonov on 2019-09-25.
 * @version 0.0.1
 */
public class GiftCertificateExtractor implements ResultSetExtractor<List<GiftCertificate>> {

    private static final String ID_COLUMN = "id";
    private static final String NAME_COLUMN = "name";
    private static final String DESCRIPTION_COLUMN = "description";
    private static final String PRICE_COLUMN = "price";
    private static final String CREATION_DATE_COLUMN = "creationdate";
    private static final String MODIFICATION_DATE_COLUMN = "modificationdate";
    private static final String EXPIRATION_DATE_COLUMN = "expirationdate";
    private static final String TAG_TITLE_COLUMN = "title";

    private String tagIdColumn;

    public GiftCertificateExtractor(String tagIdColumn) {
        this.tagIdColumn = tagIdColumn;
    }

    @Override
    public List<GiftCertificate> extractData(ResultSet resultSet) throws SQLException {
        List<GiftCertificate> giftCertificates = new LinkedList<>();
        Map<Long, GiftCertificate> table = new LinkedHashMap<>();
        while (resultSet.next()) {
            if (!table.containsKey(resultSet.getLong(ID_COLUMN))) {

                GiftCertificate giftCertificate = new GiftCertificate.Builder().build();
                giftCertificate.setId(resultSet.getLong(ID_COLUMN));
                giftCertificate.setName(resultSet.getString(NAME_COLUMN));
                giftCertificate.setDescription(resultSet.getString(DESCRIPTION_COLUMN));
                giftCertificate.setPrice(resultSet.getBigDecimal(PRICE_COLUMN));
                giftCertificate.setCreationDate(extractDate(resultSet, CREATION_DATE_COLUMN));
                giftCertificate.setModificationDate(extractDate(resultSet, MODIFICATION_DATE_COLUMN));
                giftCertificate.setExpirationDate(extractDate(resultSet, EXPIRATION_DATE_COLUMN));
                giftCertificate.setTags(new HashSet<>());

                long tagId = resultSet.getLong(tagIdColumn);
                String tagTitle = resultSet.getString(TAG_TITLE_COLUMN);
                Tag tag = new Tag(tagId, tagTitle);
                giftCertificate.getTags().add(tagId != 0 && tagTitle != null ? tag : null);
                table.put(giftCertificate.getId(), giftCertificate);
            } else {
                long tagId = resultSet.getLong(tagIdColumn);
                String tagTitle = resultSet.getString(TAG_TITLE_COLUMN);
                table.get(resultSet.getLong(ID_COLUMN)).getTags()
                        .add(tagId != 0 && tagTitle != null ? new Tag(tagId, tagTitle) : null);
            }
        }
        table.forEach((id, giftCertificate) -> giftCertificates.add(giftCertificate));
        return giftCertificates;
    }

    private LocalDate extractDate(ResultSet resultSet, String column) throws SQLException {
        return resultSet.getDate(column) != null ?
                resultSet.getDate(column).toLocalDate() : null;
    }
}
