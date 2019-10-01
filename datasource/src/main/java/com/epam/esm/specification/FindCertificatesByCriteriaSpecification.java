package com.epam.esm.specification;

import com.epam.esm.entity.criteria.LimitOffsetCriteria;
import com.epam.esm.entity.criteria.SearchCriteria;
import com.epam.esm.entity.criteria.SortCriteria;
import com.epam.esm.exception.CriteriaSearchTypeException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.jdbc.core.PreparedStatementSetter;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import static com.epam.esm.specification.SqlSpecificationConstant.*;

/**
 * to select certificate by criteria
 *
 * @author Dzmitry Platonov on 2019-09-26.
 * @version 0.0.1
 */
public class FindCertificatesByCriteriaSpecification implements SqlSpecification {

    private static final Logger log = LogManager.getLogger();

    private static final String EXCEPTION_MESSAGE = "no enum constant for found for ";

    private SearchCriteria searchCriteria;
    private SortCriteria sortCriteria;
    private LimitOffsetCriteria limitOffsetCriteria;


    public FindCertificatesByCriteriaSpecification(SearchCriteria searchCriteria, SortCriteria sortCriteria,
                                                   LimitOffsetCriteria limitOffsetCriteria) {
        this.searchCriteria = searchCriteria;
        this.sortCriteria = sortCriteria;
        this.limitOffsetCriteria = limitOffsetCriteria;
    }

    @Override
    public String sql() {
        return SQL_CERTIFICATE_BY_CRITERIA_SPECIFICATION + buildSqlWhere() +
                List.of(
                        buildIdCriteriaSqlSearchClause(),
                        buildNameCriteriaSqlSearchClause(),
                        buildDescriptionCriteriaSqlSearchClause(),
                        buildCreationDateCriteriaSqlSearchClause(),
                        buildModificationDateCriteriaSqlSearchClause(),
                        buildExpirationDateCriteriaSqlSearchClause(),
                        buildTagIdCriteriaSqlSearchClause(),
                        buildPriceCriteriaSqlSearchClause()).stream()
                        .collect(Collectors.filtering(s -> !s.isBlank(), Collectors.joining(SQL_AND))) +
                buildSortSqlClause() +
                buildLimitOffsetSqlClause();
    }

    @Override
    public PreparedStatementSetter setStatement() {
        return preparedStatement -> {
            int lastSettedField = setPreparedStatementIdCriteria(preparedStatement);
            lastSettedField = setPreparedStatementNameCriteria(preparedStatement, lastSettedField);
            lastSettedField = setPreparedStatementDescriptionCriteria(preparedStatement, lastSettedField);
            lastSettedField = setPreparedStatementCreationDateCriteria(preparedStatement, lastSettedField);
            lastSettedField = setPreparedStatementModificationDateCriteria(preparedStatement, lastSettedField);
            lastSettedField = setPreparedStatementExpirationDateCriteria(preparedStatement, lastSettedField);
            lastSettedField = setPreparedStatementTagIdCriteria(preparedStatement, lastSettedField);
            lastSettedField = setPreparedStatementPriceCriteria(preparedStatement, lastSettedField);
            setPreparedStatementLimitOffsetCriteria(preparedStatement, lastSettedField);
            log.debug(lastSettedField);
            log.debug(preparedStatement);
        };
    }

    private String buildSqlWhere() {
        if (searchCriteria.getIdCriteria() == null &&
                searchCriteria.getPriceCriteria() == null && searchCriteria.getExpirationDateCriteria() == null &&
                searchCriteria.getModificationDateCriteria() == null && searchCriteria.getCreationDateCriteria() == null &&
                searchCriteria.getDescriptionCriteria() == null && searchCriteria.getNameCriteria() == null &&
                searchCriteria.getTagCriteria() == null) {
            return "";
        } else {
            return SQL_WHERE;
        }
    }

    private void setPreparedStatementLimitOffsetCriteria(PreparedStatement preparedStatement, int lastSettedField) throws SQLException {
        if (limitOffsetCriteria != null) {
            lastSettedField++;
            if (limitOffsetCriteria.getLimit() != 0) {
                preparedStatement.setInt(lastSettedField++, limitOffsetCriteria.getLimit());
            }
            preparedStatement.setLong(lastSettedField, limitOffsetCriteria.getOffset());
        }
    }

    private String buildSortSqlClause() {
        StringBuilder sql = new StringBuilder();
        if (sortCriteria != null && !sortCriteria.getCriteriaList().isEmpty() &&
                !sortCriteria.getCriteriaList().contains("")) {
            List<String> criteriaList = sortCriteria.getCriteriaList();
            sql.append(SQL_ORDER_BY).append(criteriaList.get(0).equals(ID) ? CERTIFICATE_ID : criteriaList.get(0));
            if (!sortCriteria.isPrimaryAscending()) {
                sql.append(SQL_DESC);
            }
            for (int i = 1; i < criteriaList.size(); i++) {
                sql.append(criteriaList.get(i).equals(ID) ? CERTIFICATE_ID : criteriaList.get(i));
            }
        }
        return sql.toString();
    }

    private String buildLimitOffsetSqlClause() {
        StringBuilder sql = new StringBuilder();
        if (limitOffsetCriteria != null) {
            if (limitOffsetCriteria.getLimit() != 0) {
                sql.append(SQL_LIMIT);
            }
            sql.append(SQL_OFFSET);
        }
        return sql.toString();
    }

    private String buildIdCriteriaSqlSearchClause() {
        String idSql = "";
        if (searchCriteria != null && searchCriteria.getIdCriteria() != null) {
            switch (searchCriteria.getIdCriteria().getParameterSearchType()) {
                case IN:
                    idSql = SQL_ID_IN + searchCriteria.getIdCriteria().getCriteriaList().stream()
                            .map(l -> SQL_PARAMETER).collect(Collectors.joining(",")) + SQL_CLOSE_IN;
                    break;
                case NOT_IN:
                    idSql = SQL_ID_NOT_IN + searchCriteria.getIdCriteria().getCriteriaList().stream()
                            .map(l -> SQL_PARAMETER).collect(Collectors.joining(",")) + SQL_CLOSE_IN;
                    break;
                case BETWEEN:
                    idSql = SQL_ID_BETWEEN;
                    break;
                case NOT_BETWEEN:
                    idSql = SQL_ID_NOT_BETWEEN;
                    break;
                default:
                    throw new CriteriaSearchTypeException(EXCEPTION_MESSAGE
                            + searchCriteria.getIdCriteria().getParameterSearchType());
            }
        }
        return idSql;
    }

    private String buildPriceCriteriaSqlSearchClause() {
        String priceSql = "";
        if (searchCriteria != null && searchCriteria.getPriceCriteria() != null) {
            switch (searchCriteria.getPriceCriteria().getParameterSearchType()) {
                case IN:
                    priceSql = SQL_PRICE_IN + searchCriteria.getPriceCriteria().getCriteriaList().stream()
                            .map(l -> SQL_PARAMETER).collect(Collectors.joining(",")) + SQL_CLOSE_IN;
                    break;
                case NOT_IN:
                    priceSql = SQL_PRICE_NOT_IN + searchCriteria.getPriceCriteria().getCriteriaList().stream()
                            .map(l -> SQL_PARAMETER).collect(Collectors.joining(",")) + SQL_CLOSE_IN;
                    break;
                case BETWEEN:
                    priceSql = SQL_PRICE_BETWEEN;
                    break;
                case NOT_BETWEEN:
                    priceSql = SQL_PRICE_NOT_BETWEEN;
                    break;
                default:
                    throw new CriteriaSearchTypeException(EXCEPTION_MESSAGE
                            + searchCriteria.getPriceCriteria().getParameterSearchType());
            }
        }
        return priceSql;
    }

    private int setPreparedStatementPriceCriteria(PreparedStatement preparedStatement, int lastSettedField) throws SQLException {
        if (searchCriteria != null && searchCriteria.getPriceCriteria() != null) {
            switch (searchCriteria.getPriceCriteria().getParameterSearchType()) {
                case IN:
                case NOT_IN:
                    for (BigDecimal price : searchCriteria.getPriceCriteria().getCriteriaList()) {
                        lastSettedField++;
                        preparedStatement.setBigDecimal(lastSettedField, price);
                    }
                    break;
                case BETWEEN:
                case NOT_BETWEEN:
                    lastSettedField++;
                    preparedStatement.setBigDecimal(lastSettedField++, searchCriteria.getPriceCriteria().getCriteriaList().get(0));
                    preparedStatement.setBigDecimal(lastSettedField, searchCriteria.getPriceCriteria().getCriteriaList().get(1));
                    break;
                default:
                    throw new CriteriaSearchTypeException(EXCEPTION_MESSAGE
                            + searchCriteria.getCreationDateCriteria().getParameterSearchType());
            }
        }
        return lastSettedField;
    }

    private int setPreparedStatementIdCriteria(PreparedStatement preparedStatement) throws SQLException {
        int lastSettedField = 0;
        if (searchCriteria != null && searchCriteria.getIdCriteria() != null) {
            switch (searchCriteria.getIdCriteria().getParameterSearchType()) {
                case IN:
                case NOT_IN:
                    for (long id : searchCriteria.getIdCriteria().getCriteriaList()) {
                        lastSettedField++;
                        preparedStatement.setLong(lastSettedField, id);
                    }
                    break;
                case BETWEEN:
                case NOT_BETWEEN:
                    lastSettedField++;
                    preparedStatement.setLong(lastSettedField++, searchCriteria.getIdCriteria().getCriteriaList().get(0));
                    preparedStatement.setLong(lastSettedField, searchCriteria.getIdCriteria().getCriteriaList().get(1));
                    break;
                default:
                    throw new CriteriaSearchTypeException(EXCEPTION_MESSAGE
                            + searchCriteria.getIdCriteria().getParameterSearchType());
            }
        }
        return lastSettedField;
    }

    private String buildNameCriteriaSqlSearchClause() {
        String nameSql = "";
        if (searchCriteria != null && searchCriteria.getNameCriteria() != null) {
            switch (searchCriteria.getNameCriteria().getSearchType()) {
                case IN:
                    nameSql = SQL_NAME_IN + searchCriteria.getNameCriteria().getCriteriaList().stream()
                            .map(l -> SQL_PARAMETER).collect(Collectors.joining(",")) + SQL_CLOSE_IN;
                    break;
                case NOT_IN:
                    nameSql = SQL_NAME_NOT_IN + searchCriteria.getNameCriteria().getCriteriaList().stream()
                            .map(l -> SQL_PARAMETER).collect(Collectors.joining(",")) + SQL_CLOSE_IN;
                    break;
                case LIKE:
                    nameSql = SQL_NAME_LIKE;
                    break;
                case NOT_LIKE:
                    nameSql = SQL_NAME_NOT_LIKE;
                    break;
                default:
                    throw new CriteriaSearchTypeException(EXCEPTION_MESSAGE
                            + searchCriteria.getNameCriteria().getSearchType());
            }
        }
        return nameSql;
    }

    private int setPreparedStatementNameCriteria(PreparedStatement preparedStatement, int lastSettedField) throws SQLException {
        if (searchCriteria != null && searchCriteria.getNameCriteria() != null) {
            switch (searchCriteria.getNameCriteria().getSearchType()) {
                case IN:
                case NOT_IN:
                    for (String name : searchCriteria.getNameCriteria().getCriteriaList()) {
                        lastSettedField++;
                        preparedStatement.setString(lastSettedField, name);
                    }
                    break;
                case LIKE:
                case NOT_LIKE:
                    lastSettedField++;
                    preparedStatement.setString(lastSettedField,
                            "%" + searchCriteria.getNameCriteria().getCriteriaList().get(0) + "%");
                    break;
                default:
                    throw new CriteriaSearchTypeException(EXCEPTION_MESSAGE
                            + searchCriteria.getNameCriteria().getSearchType());
            }
        }
        return lastSettedField;
    }

    private String buildDescriptionCriteriaSqlSearchClause() {
        String descriptionSql = "";
        if (searchCriteria != null && searchCriteria.getDescriptionCriteria() != null) {
            switch (searchCriteria.getDescriptionCriteria().getSearchType()) {
                case IN:
                    descriptionSql = SQL_DESCRIPTION_IN + searchCriteria.getDescriptionCriteria().getCriteriaList().stream()
                            .map(l -> SQL_PARAMETER).collect(Collectors.joining(",")) + SQL_CLOSE_IN;
                    break;
                case NOT_IN:
                    descriptionSql = SQL_DESCRIPTION_NOT_IN + searchCriteria.getDescriptionCriteria().getCriteriaList().stream()
                            .map(l -> SQL_PARAMETER).collect(Collectors.joining(",")) + SQL_CLOSE_IN;
                    break;
                case LIKE:
                    descriptionSql = SQL_DESCRITION_LIKE;
                    break;
                case NOT_LIKE:
                    descriptionSql = SQL_DESCRIPTION_NOT_LIKE;
                    break;
                default:
                    throw new CriteriaSearchTypeException(EXCEPTION_MESSAGE
                            + searchCriteria.getDescriptionCriteria().getSearchType());
            }
        }
        return descriptionSql;
    }

    private int setPreparedStatementDescriptionCriteria(PreparedStatement preparedStatement, int lastSettedField) throws SQLException {
        if (searchCriteria != null && searchCriteria.getDescriptionCriteria() != null) {
            switch (searchCriteria.getDescriptionCriteria().getSearchType()) {
                case IN:
                case NOT_IN:
                    for (String description : searchCriteria.getDescriptionCriteria().getCriteriaList()) {
                        lastSettedField++;
                        preparedStatement.setString(lastSettedField, description);
                    }
                    break;
                case LIKE:
                case NOT_LIKE:
                    lastSettedField++;
                    preparedStatement.setString(lastSettedField,
                            "%" + searchCriteria.getDescriptionCriteria().getCriteriaList().get(0) + "%");
                    break;
                default:
                    throw new CriteriaSearchTypeException(EXCEPTION_MESSAGE
                            + searchCriteria.getDescriptionCriteria().getSearchType());
            }
        }
        return lastSettedField;
    }

    private String buildCreationDateCriteriaSqlSearchClause() {
        String creationDateSql = "";
        if (searchCriteria != null && searchCriteria.getCreationDateCriteria() != null) {
            switch (searchCriteria.getCreationDateCriteria().getParameterSearchType()) {
                case IN:
                    creationDateSql = SQL_CREATION_DATE_IN + searchCriteria.getCreationDateCriteria().getCriteriaList().stream()
                            .map(l -> SQL_PARAMETER).collect(Collectors.joining(",")) + SQL_CLOSE_IN;
                    break;
                case NOT_IN:
                    creationDateSql = SQL_CREATION_DATE_NOT_IN + searchCriteria.getCreationDateCriteria().getCriteriaList().stream()
                            .map(l -> SQL_PARAMETER).collect(Collectors.joining(",")) + SQL_CLOSE_IN;
                    break;
                case BETWEEN:
                    creationDateSql = SQL_CREATION_DATE_BETWEEN;
                    break;
                case NOT_BETWEEN:
                    creationDateSql = SQL_CREATION_DATE_NOT_BETWEEN;
                    break;
                default:
                    throw new CriteriaSearchTypeException(EXCEPTION_MESSAGE
                            + searchCriteria.getCreationDateCriteria().getParameterSearchType());
            }
        }
        return creationDateSql;
    }

    private int setPreparedStatementCreationDateCriteria(PreparedStatement preparedStatement, int lastSettedField) throws SQLException {
        if (searchCriteria != null && searchCriteria.getCreationDateCriteria() != null) {
            switch (searchCriteria.getCreationDateCriteria().getParameterSearchType()) {
                case IN:
                case NOT_IN:
                    for (LocalDate date : searchCriteria.getCreationDateCriteria().getCriteriaList()) {
                        lastSettedField++;
                        preparedStatement.setDate(lastSettedField, Date.valueOf(date));
                    }
                    break;
                case BETWEEN:
                case NOT_BETWEEN:
                    lastSettedField++;
                    preparedStatement.setDate(lastSettedField++, Date.valueOf(searchCriteria.getCreationDateCriteria().getCriteriaList().get(0)));
                    preparedStatement.setDate(lastSettedField, Date.valueOf(searchCriteria.getCreationDateCriteria().getCriteriaList().get(1)));
                    break;
                default:
                    throw new CriteriaSearchTypeException(EXCEPTION_MESSAGE
                            + searchCriteria.getCreationDateCriteria().getParameterSearchType());
            }
        }
        return lastSettedField;
    }

    private String buildModificationDateCriteriaSqlSearchClause() {
        String modificationDateSql = "";
        if (searchCriteria != null && searchCriteria.getModificationDateCriteria() != null) {
            switch (searchCriteria.getModificationDateCriteria().getParameterSearchType()) {
                case IN:
                    modificationDateSql = SQL_MODIFICATION_DATE_IN + searchCriteria.getModificationDateCriteria().getCriteriaList().stream()
                            .map(l -> SQL_PARAMETER).collect(Collectors.joining(",")) + SQL_CLOSE_IN;
                    break;
                case NOT_IN:
                    modificationDateSql = SQL_MODIFICATION_DATE_NOT_IN + searchCriteria.getModificationDateCriteria().getCriteriaList().stream()
                            .map(l -> SQL_PARAMETER).collect(Collectors.joining(",")) + SQL_CLOSE_IN;
                    break;
                case BETWEEN:
                    modificationDateSql = SQL_MODIFICATION_DATE_BETWEEN;
                    break;
                case NOT_BETWEEN:
                    modificationDateSql = SQL_MODIFICATION_DATE_NOT_BETWEEN;
                    break;
                default:
                    throw new CriteriaSearchTypeException(EXCEPTION_MESSAGE
                            + searchCriteria.getModificationDateCriteria().getParameterSearchType());
            }
        }
        return modificationDateSql;
    }

    private int setPreparedStatementModificationDateCriteria(PreparedStatement preparedStatement, int lastSettedField) throws SQLException {
        if (searchCriteria != null && searchCriteria.getModificationDateCriteria() != null) {
            switch (searchCriteria.getModificationDateCriteria().getParameterSearchType()) {
                case IN:
                case NOT_IN:
                    for (LocalDate date : searchCriteria.getModificationDateCriteria().getCriteriaList()) {
                        lastSettedField++;
                        preparedStatement.setDate(lastSettedField, Date.valueOf(date));
                    }
                    break;
                case BETWEEN:
                case NOT_BETWEEN:
                    lastSettedField++;
                    preparedStatement.setDate(lastSettedField++, Date.valueOf(searchCriteria.getModificationDateCriteria().getCriteriaList().get(0)));
                    preparedStatement.setDate(lastSettedField, Date.valueOf(searchCriteria.getModificationDateCriteria().getCriteriaList().get(1)));
                    break;
                default:
                    throw new CriteriaSearchTypeException(EXCEPTION_MESSAGE
                            + searchCriteria.getModificationDateCriteria().getParameterSearchType());
            }
        }
        return lastSettedField;
    }

    private String buildExpirationDateCriteriaSqlSearchClause() {
        String expirationDateSql = "";
        if (searchCriteria != null && searchCriteria.getExpirationDateCriteria() != null) {
            switch (searchCriteria.getExpirationDateCriteria().getSearchType()) {
                case IN:
                    expirationDateSql = SQL_EXPIRATION_DATE_IN + searchCriteria.getExpirationDateCriteria().getCriteriaList().stream()
                            .map(l -> SQL_PARAMETER).collect(Collectors.joining(",")) + SQL_CLOSE_IN;
                    break;
                case NOT_IN:
                    expirationDateSql = SQL_EXPIRATION_DATE_NOT_IN + searchCriteria.getExpirationDateCriteria().getCriteriaList().stream()
                            .map(l -> SQL_PARAMETER).collect(Collectors.joining(",")) + SQL_CLOSE_IN;
                    break;
                case BETWEEN:
                    expirationDateSql = SQL_EXPIRATION_DATE_BETWEEN;
                    break;
                case NOT_BETWEEN:
                    expirationDateSql = SQL_EXPIRATION_DATE_NOT_BETWEEN;
                    break;
                default:
                    throw new CriteriaSearchTypeException(EXCEPTION_MESSAGE
                            + searchCriteria.getExpirationDateCriteria().getSearchType());
            }
        }
        return expirationDateSql;
    }

    private int setPreparedStatementExpirationDateCriteria(PreparedStatement preparedStatement, int lastSettedField) throws SQLException {
        if (searchCriteria != null && searchCriteria.getExpirationDateCriteria() != null) {
            switch (searchCriteria.getExpirationDateCriteria().getSearchType()) {
                case IN:
                case NOT_IN:
                    for (LocalDate date : searchCriteria.getExpirationDateCriteria().getCriteriaList()) {
                        lastSettedField++;
                        preparedStatement.setDate(lastSettedField, Date.valueOf(date));
                    }
                    break;
                case BETWEEN:
                case NOT_BETWEEN:
                    lastSettedField++;
                    preparedStatement.setDate(lastSettedField++, Date.valueOf(searchCriteria.getExpirationDateCriteria().getCriteriaList().get(0)));
                    preparedStatement.setDate(lastSettedField, Date.valueOf(searchCriteria.getExpirationDateCriteria().getCriteriaList().get(1)));
                    break;
                default:
                    throw new CriteriaSearchTypeException(EXCEPTION_MESSAGE
                            + searchCriteria.getExpirationDateCriteria().getSearchType());
            }
        }
        return lastSettedField;
    }

    private String buildTagIdCriteriaSqlSearchClause() {
        String tagIdSql = "";
        if (searchCriteria != null && searchCriteria.getTagCriteria() != null) {
            switch (searchCriteria.getTagCriteria().getParameterSearchType()) {
                case IN:
                    tagIdSql = SQL_TAG_ID_IN + searchCriteria.getTagCriteria().getTagIds().stream()
                            .map(l -> SQL_PARAMETER).collect(Collectors.joining(",")) + SQL_CLOSE_IN;
                    break;
                case NOT_IN:
                    tagIdSql = SQL_TAG_ID_NOT_IN + searchCriteria.getTagCriteria().getTagIds().stream()
                            .map(l -> SQL_PARAMETER).collect(Collectors.joining(",")) + SQL_CLOSE_IN + SQL_CLOSE_IN;
                    break;
                case BETWEEN:
                    tagIdSql = SQL_TAG_ID_BETWEEN;
                    break;
                case NOT_BETWEEN:
                    tagIdSql = SQL_TAG_ID_NOT_BETWEEN;
                    break;
                default:
                    throw new CriteriaSearchTypeException(EXCEPTION_MESSAGE
                            + searchCriteria.getTagCriteria().getTagIds());
            }
        }
        return tagIdSql;
    }

    private int setPreparedStatementTagIdCriteria(PreparedStatement preparedStatement, int lastSettedField) throws SQLException {
        if (searchCriteria != null && searchCriteria.getTagCriteria() != null) {
            switch (searchCriteria.getTagCriteria().getParameterSearchType()) {
                case IN:
                case NOT_IN:
                    for (long id : searchCriteria.getTagCriteria().getTagIds()) {
                        lastSettedField++;
                        preparedStatement.setLong(lastSettedField, id);
                    }
                    break;
                case BETWEEN:
                case NOT_BETWEEN:
                    lastSettedField++;
                    preparedStatement.setLong(lastSettedField++, searchCriteria.getTagCriteria().getTagIds().get(0));
                    preparedStatement.setLong(lastSettedField, searchCriteria.getTagCriteria().getTagIds().get(1));
                    break;
                default:
                    throw new CriteriaSearchTypeException(EXCEPTION_MESSAGE
                            + searchCriteria.getIdCriteria().getParameterSearchType());
            }
        }
        return lastSettedField;
    }
}
