package com.epam.esm.repository;

/**
 * Sql constants to be used in repository methods
 *
 * @author Dzmitry Platonov on 2019-09-30.
 * @version 0.0.1
 */
public class SqlConstant {

    private SqlConstant() {
    }

    static final String SQL_INSERT_CERTIFICATE =
            "insert into certificate(name, description, price, creationdate, modificationdate, expirationdate) " +
                    "values (?, ?, ?, ?, ?, ?)";

    static final String SQL_CERTIFICATE_INSERT_LINK = "insert into certificate_tag(certificate_id, tag_id) values (?, ?);";
    static final String SQL_CERTIFICATE_INSERT_TAG = "insert into tag(title) values (?)";
    static final String SQL_DETECT_TAG = "select id, title from tag where title = ?;";
    static final String SQL_CERTIFICATE_DELETE = "delete from certificate where id = ?;";
    static final String SQL_CERTIFICATE_DELETE_LINK = "delete from certificate_tag where certificate_id = ?;";
    static final String SQL_CERTIFICATE_UPDATE =
            "update certificate set name = ?, description = ?, price = ?, modificationdate = ?," +
                    " expirationdate = ? where id = ?;";
    static final String SQL_SELECT_CERTIFICATE_BY_ID =
            "select certificate.id, name, description, price, creationdate, modificationdate, expirationdate," +
                    " certificate_id, tag_id, tag.id, title from certificate " +
                    "left join certificate_tag on certificate.id = certificate_id " +
                    "left join tag on certificate_tag.tag_id = tag.id where certificate.id = ?";

    static final String SQL_SELECT_TAG_ALL = "select id, title from tag";
    static final String SQL_SELECT_ALL_CERTIFICATES =
            "select certificate.id, name, description, price, creationdate, modificationdate, expirationdate, certificate_id, " +
                    "tag_id, tag.id, title from certificate left join certificate_tag on certificate.id = certificate_id " +
                    "left join tag on certificate_tag.tag_id = tag.id";


    static final String SQL_TAG_INSERT = "insert into tag (title) values (?)";
    static final String SQL_TAG_DELETE = "delete from tag where id = ?;";
    static final String SQL_TAG_DELETE_LINK = "delete from certificate_tag where tag_id = ?;";
    static final String SQL_SELECT_TAG_BY_ID = "select id, title from tag where id = ?";

    public static final String SQL_TAG_ID_COLUMN = "id";
    public static final String SQL_TAG_TITLE_COLUMN = "title";
    static final String CERTIFICATE_EXTRACTOR_TAG_ID_COLUMN = "tag_id";


}
