package com.epam.esm.converter;

import com.epam.esm.entity.criteria.ParameterSearchType;
import com.epam.esm.entity.criteria.TextSearchType;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.epam.esm.converter.ConverterConstant.BETWEEN_KEY;
import static com.epam.esm.converter.ConverterConstant.LIKE_KEY;
import static com.epam.esm.converter.ConverterConstant.NOT_BETWEEN_KEY;
import static com.epam.esm.converter.ConverterConstant.NOT_IN_KEY;
import static com.epam.esm.converter.ConverterConstant.NOT_LIKE_KEY;

/**
 * helper to provide methods for {@link CriteriaCreator}
 *
 * @author Dzmitry Platonov on 2019-10-02.
 * @version 0.0.1
 */
@Component
public class CriteriaCreatorHelper<T> {

    List<Object> buildParameters(String criteria) {
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

    TextSearchType buildTextSearchType(String criteria) {
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

    String cutPrefixCriteriaString(String criteria) {
        if (criteria.startsWith(NOT_IN_KEY)) return criteria.replaceFirst(NOT_IN_KEY, "");
        else if (criteria.startsWith(LIKE_KEY)) return criteria.replaceFirst(LIKE_KEY, "");
        else if (!criteria.startsWith(NOT_LIKE_KEY)) return criteria;
        else return criteria.replaceFirst(NOT_LIKE_KEY, "");
    }

    T createDateCriteria(Map<String, String> criteriaMap, String parameterName, DateCreationExecutor<T> executor) {
        String requestParameter = criteriaMap.get(parameterName);
        if (requestParameter == null || requestParameter.isBlank()) {
            return null;
        }
        List<Object> parameters = buildParameters(requestParameter);
        ParameterSearchType parameterSearchType = (ParameterSearchType) parameters.get(0);
        requestParameter = String.valueOf(parameters.get(1));
        List<LocalDate> creationList = Arrays.stream(requestParameter.split(","))
                .map(LocalDate::parse)
                .collect(Collectors.toList());

        return executor.execute(parameterSearchType, creationList);
    }
}
