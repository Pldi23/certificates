package com.epam.esm.specification;

/**
 * constants to be used for {@link SqlSpecification}'s
 *
 * @author Dzmitry Platonov on 2019-09-30.
 * @version 0.0.1
 */
class SqlSpecificationConstant {

    private SqlSpecificationConstant() {
    }

    static final String SQL_CERTIFICATE_BY_CRITERIA_SPECIFICATION = "select certificate.id, name, description, price, creationdate," +
            " modificationdate, expirationdate, certificate_id, tag_id, tag.id, title from certificate " +
            "join certificate_tag on certificate.id = certificate_id " +
            "left join tag on certificate_tag.tag_id = tag.id";

    static final String SQL_TAG_BY_ID_SPECIFICATION = "select * from tag where id = ?";
    static final String SQL_TAG_BY_TITLE_SPECIFICATION = "select * from tag where title like ?";
    static final String SQL_CERTIFICATE_BY_ID_SPECIFICATION =
            "select * from certificate join certificate_tag on certificate.id = certificate_id " +
                    "left join tag on certificate_tag.tag_id = tag.id where certificate.id = ?";
    static final String SQL_TAG_ALL_SPECIFICATION = "select * from tag";
    static final String SQL_CERTIFICATES_ALL_SPECIFICATION =
            "select * from certificate join certificate_tag on certificate.id = certificate_id " +
                    "left join tag on certificate_tag.tag_id = tag.id";

    static final String SQL_WHERE = " where ";


    static final String SQL_ID_BETWEEN = "certificate.id between ? and ? ";
    static final String SQL_ID_NOT_BETWEEN = "certificate.id not between ? and ? ";
    static final String SQL_ID_IN = "certificate.id in (";
    static final String SQL_ID_NOT_IN = "certificate.id not in (";

    static final String SQL_AND = " and ";
    static final String SQL_PARAMETER = "?";
    static final String SQL_CLOSE_IN = ") ";

    static final String SQL_NAME_IN = "name in (";
    static final String SQL_NAME_NOT_IN = "name not in (";
    static final String SQL_NAME_LIKE = "name like lower(?)";
    static final String SQL_NAME_NOT_LIKE = "name not like (?)";

    static final String SQL_DESCRIPTION_IN = "description in (";
    static final String SQL_DESCRIPTION_NOT_IN = "description not in (";
    static final String SQL_DESCRITION_LIKE = "description like lower(?)";
    static final String SQL_DESCRIPTION_NOT_LIKE = "description not like (?)";

    static final String SQL_CREATION_DATE_BETWEEN = "creationdate between ? and ?";
    static final String SQL_CREATION_DATE_NOT_BETWEEN = "creationdate not between ? and ?";
    static final String SQL_CREATION_DATE_IN = "creationdate in (";
    static final String SQL_CREATION_DATE_NOT_IN = "creationdate not in (";

    static final String SQL_MODIFICATION_DATE_BETWEEN = "modificationdate between ? and ?";
    static final String SQL_MODIFICATION_DATE_NOT_BETWEEN = "modificationdate not between ? and ?";
    static final String SQL_MODIFICATION_DATE_IN = "modificationdate in (";
    static final String SQL_MODIFICATION_DATE_NOT_IN = "modificationdate not in (";

    static final String SQL_EXPIRATION_DATE_BETWEEN = "expirationdate between ? and ?";
    static final String SQL_EXPIRATION_DATE_NOT_BETWEEN = "expirationdate not between ? and ?";
    static final String SQL_EXPIRATION_DATE_IN = "expirationdate in (";
    static final String SQL_EXPIRATION_DATE_NOT_IN = "expirationdate not in (";

    static final String SQL_PRICE_BETWEEN = "price between ? and ?";
    static final String SQL_PRICE_NOT_BETWEEN = "price not between ? and ?";
    static final String SQL_PRICE_IN = "price in (";
    static final String SQL_PRICE_NOT_IN = "price not in (";

    static final String SQL_TAG_ID_BETWEEN =
            "certificate_id in (select certificate_id from certificate_tag where tag_id between ? and ?) ";
    static final String SQL_TAG_ID_NOT_BETWEEN =
            "certificate_id in (select certificate_id from certificate_tag where tag_id not between ? and ?) ";
    static final String SQL_TAG_ID_IN = "certificate_id in (select certificate_id from certificate_tag where tag_id in (";
    static final String SQL_TAG_ID_NOT_IN = "certificate_id in (select certificate_id from certificate_tag where tag_id not in (";

    static final String ID = "id";
    static final String CERTIFICATE_ID = "certificate.id";
    static final String SQL_LIMIT = " limit ? ";
    static final String SQL_OFFSET = " offset ? ";
    static final String SQL_ORDER_BY = " order by ";
    static final String SQL_DESC = " desc ";
}
