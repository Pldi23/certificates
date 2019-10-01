package com.epam.esm.converter;

import com.epam.esm.entity.criteria.*;
import com.epam.esm.exception.CriteriaSearchTypeException;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.epam.esm.converter.ConverterConstant.*;

/**
 * gift certificates
 *
 * @author Dzmitry Platonov on 2019-09-27.
 * @version 0.0.1
 */
@Component
public class ConverterFactory {


    Criteria buildCriteria(Map<String, String> criteriaMap, String key) {
        Criteria criteria;
        String criteriaString = criteriaMap.get(key);
        if (criteriaString == null || criteriaString.isBlank()) {
            return null;
        }
        List<Object> parameters;
        ParameterSearchType parameterSearchType;
        TextSearchType textSearchType;
        switch (key) {
            case ID:
                parameters = buildParameters(criteriaString);
                parameterSearchType = (ParameterSearchType) parameters.get(0);
                criteriaString = String.valueOf(parameters.get(1));
                List<Long> criteriaIdList = Arrays.stream(criteriaString.split(","))
                        .map(Long::parseLong).collect(Collectors.toList());
                criteria = new IdCriteria(parameterSearchType, criteriaIdList);
                break;

            case NAME:
                textSearchType = buildTextSearchType(criteriaString);
                criteriaString = cutPrefixCriteriaString(criteriaString);
                List<String> criteriaNameList = Arrays.asList(criteriaString.split(","));
                criteria = new NameCriteria(textSearchType, criteriaNameList);
                break;
            case PRICE:
                parameters = buildParameters(criteriaString);
                parameterSearchType = (ParameterSearchType) parameters.get(0);
                criteriaString = String.valueOf(parameters.get(1));
                List<BigDecimal> criteriaList = Arrays.stream(criteriaString.split(","))
                        .map(BigDecimal::new).collect(Collectors.toList());
                criteria = new PriceCriteria(parameterSearchType, criteriaList);
                break;
            case CREATION_DATE:
                parameters = buildParameters(criteriaString);
                parameterSearchType = (ParameterSearchType) parameters.get(0);
                criteriaString = String.valueOf(parameters.get(1));
                List<LocalDate> creationList = Arrays.stream(criteriaString.split(","))
                        .map(LocalDate::parse)
                        .collect(Collectors.toList());
                criteria = new CreationDateCriteria(parameterSearchType, creationList);
                break;
            case MODIFICATION_DATE:
                parameters = buildParameters(criteriaString);
                parameterSearchType = (ParameterSearchType) parameters.get(0);
                criteriaString = String.valueOf(parameters.get(1));
                List<LocalDate> modificationList = Arrays.stream(criteriaString.split(","))
                        .map(LocalDate::parse)
                        .collect(Collectors.toList());
                criteria = new ModificationDateCriteria(parameterSearchType, modificationList);
                break;
            case EXPIRATION_DATE:
                parameters = buildParameters(criteriaString);
                parameterSearchType = (ParameterSearchType) parameters.get(0);
                criteriaString = String.valueOf(parameters.get(1));
                List<LocalDate> expirationDateList = Arrays.stream(criteriaString.split(","))
                        .map(LocalDate::parse)
                        .collect(Collectors.toList());
                criteria = new ExpirationDateCriteria(parameterSearchType, expirationDateList);
                break;
            case DESCRIPTION:
                TextSearchType descriptionSearchType = buildTextSearchType(criteriaString);
                criteriaString = cutPrefixCriteriaString(criteriaString);
                List<String> descriptionList = Arrays.asList(criteriaString.split(","));
                criteria = new DescriptionCriteria(descriptionSearchType, descriptionList);
                break;
            case TAG_ID:
                parameters = buildParameters(criteriaString);
                parameterSearchType = (ParameterSearchType) parameters.get(0);
                criteriaString = String.valueOf(parameters.get(1));
                List<Long> tagsList = Arrays.stream(criteriaString.split(","))
                        .map(Long::parseLong).collect(Collectors.toList());
                criteria = new TagCriteria(parameterSearchType, tagsList);
                break;
            default:
                throw new CriteriaSearchTypeException("could not possible to build criteria for parameter: " + criteriaString);
        }
        return criteria;
    }


    private List<Object> buildParameters(String criteria) {
        ParameterSearchType parameterSearchType;
        if (criteria.startsWith(NOT_IN_KEY)) {
            parameterSearchType = ParameterSearchType.NOT_IN;
            criteria = criteria.replaceFirst(NOT_IN_KEY, "");
        } else if (criteria.startsWith(BETWEEN_KEY)) {
            parameterSearchType = ParameterSearchType.BETWEEN;
            criteria = criteria.replaceFirst(BETWEEN_KEY, "");
        } else if (criteria.startsWith(NOT_BETWEEN_KEY)) {
            parameterSearchType = ParameterSearchType.NOT_BETWEEN;
            criteria = criteria.replaceFirst(NOT_BETWEEN_KEY, "");
        } else {
            parameterSearchType = ParameterSearchType.IN;
        }
        return List.of(parameterSearchType, criteria);
    }


    private TextSearchType buildTextSearchType(String criteria) {
        TextSearchType searchType;
        if (criteria.startsWith(NOT_IN_KEY)) {
            searchType = TextSearchType.NOT_IN;
        } else if (criteria.startsWith(LIKE_KEY)) {
            searchType = TextSearchType.LIKE;
        } else if (criteria.startsWith(NOT_LIKE_KEY)) {
            searchType = TextSearchType.NOT_LIKE;
        } else {
            searchType = TextSearchType.IN;
        }
        return searchType;
    }

    private String cutPrefixCriteriaString(String criteria) {
        if (criteria.startsWith(NOT_IN_KEY)) return criteria.replaceFirst(NOT_IN_KEY, "");
        else if (criteria.startsWith(LIKE_KEY)) return criteria.replaceFirst(LIKE_KEY, "");
        else if (!criteria.startsWith(NOT_LIKE_KEY)) return criteria;
        else return criteria.replaceFirst(NOT_LIKE_KEY, "");
    }
}
