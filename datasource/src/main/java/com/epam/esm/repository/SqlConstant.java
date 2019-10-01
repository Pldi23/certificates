package com.epam.esm.repository;

/**
 * gift certificates
 *
 * @author Dzmitry Platonov on 2019-09-30.
 * @version 0.0.1
 */
class SqlConstant {

    private SqlConstant() {
    }

    static final String SQL_INSERT_CERTIFICATE =
            "insert into certificate(name, description, price, creationdate, modificationdate, expirationdate) " +
                    "values (?, ?, ?, ?, ?, ?)";

    static final String SQL_CERTIFICATE_INSERT_LINK = "insert into certificate_tag(certificate_id, tag_id) values (?, ?);";
    static final String SQL_CERTIFICATE_INSERT_TAG = "insert into tag(title) values (?)";
    static final String SQL_CERTIFICATE_DETECT_TAG = "select count(*) from tag where id = ?;";
    static final String SQL_CERTIFICATE_DELETE = "delete from certificate where id = ?;";
    static final String SQL_CERTIFICATE_DELETE_LINK = "delete from certificate_tag where certificate_id = ?;";
    static final String SQL_CERTIFICATE_UPDATE =
            "update certificate set name = ?, description = ?, price = ?, creationdate = ?, modificationdate = ?," +
                    " expirationdate = ? where id = ?;";

    static final String SQL_SELECT_CERTIFICATES_BY_TAG = "select * from get_certificates_by_tag(?)";
    static final String CERTIFICATE_EXTRACTOR_TAG_ID_COLUMN = "tag_id";
    static final String CERTIFICATE_EXTRACTOR_OUT_TAG_ID = "out_tag_id";

    static final String SQL_TAG_INSERT = "insert into tag (title) values (?)";
    static final String SQL_TAG_DELETE = "delete from tag where id = ?;";
    static final String SQL_TAG_DELETE_LINK = "delete from certificate_tag where tag_id = ?;";
    static final String SQL_GET_TAGS_BY_CERTIFICATE_FUNCTION = "select * from get_tags_by_certificate(?)";
}
