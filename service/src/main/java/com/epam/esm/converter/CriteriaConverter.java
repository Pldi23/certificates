package com.epam.esm.converter;


import com.epam.esm.dto.*;
import com.epam.esm.entity.criteria.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static com.epam.esm.converter.ConvertConstant.*;

/**
 * gift certificates
 *
 * @author Dzmitry Platonov on 2019-09-26.
 * @version 0.0.1
 */
@Component
public class CriteriaConverter {

    private ConverterFactory converterFactory;

    @Autowired
    public CriteriaConverter(ConverterFactory converterFactory) {
        this.converterFactory = converterFactory;
    }

    public SearchCriteria convertSearchCriteria(SearchCriteriaRequestDTO searchCriteriaRequestDTO) {

        Map<String, String> criteriaMap = searchCriteriaRequestDTO.getParameters();

        return new SearchCriteria.Builder()
                .withIdCriteria((IdCriteria) converterFactory.buildCriteria(criteriaMap, ID))
                .withNameCriteria((NameCriteria) converterFactory.buildCriteria(criteriaMap, NAME))
                .withDescriptionCriteria((DescriptionCriteria) converterFactory.buildCriteria(criteriaMap, DESCRIPTION))
                .withPriceCriteria((PriceCriteria) converterFactory.buildCriteria(criteriaMap, PRICE))
                .withCreationDateCriteria((CreationDateCriteria) converterFactory.buildCriteria(criteriaMap, CREATION_DATE))
                .withExpirationDateCriteria((ExpirationDateCriteria) converterFactory.buildCriteria(criteriaMap, EXPIRATION_DATE))
                .withModificationDateCriteria((ModificationDateCriteria) converterFactory.buildCriteria(criteriaMap, MODIFICATION_DATE))
                .withTagCriteria((TagCriteria) converterFactory.buildCriteria(criteriaMap, TAG_ID))
                .build();
    }

    public LimitOffsetCriteria convertLimitOffsetCriteria(LimitOffsetCriteriaRequestDTO limitOffsetCriteriaRequestDTO) {
        Map<String, String> criteriaMap = limitOffsetCriteriaRequestDTO.getParameters();
        int limit = 0;
        long offset = 0;
        if (criteriaMap.containsKey(LIMIT) && criteriaMap.get(LIMIT) != null && !criteriaMap.get(LIMIT).isBlank()) {
            limit = Integer.parseInt(criteriaMap.get(LIMIT));
        }
        if (criteriaMap.containsKey(OFFSET) && criteriaMap.get(OFFSET) != null && !criteriaMap.get(OFFSET).isBlank()) {
            offset = Long.parseLong(criteriaMap.get(OFFSET));
        }
        return new LimitOffsetCriteria.Builder()
                .withLimit(limit)
                .withOffset(offset)
                .build();
    }

    public SortCriteria convertSortCriteria(SortCriteriaRequestDTO sortCriteriaRequestDTO) {
        String criteria = sortCriteriaRequestDTO.getParameters().get(SORT);
        if (criteria == null) {
            return null;
        }
        boolean isAscending = !criteria.startsWith(SORT_DESCENDING_KEY);
        criteria = criteria.replaceFirst(SORT_DESCENDING_KEY, "");
        List<String> criteriaList = Arrays.asList(criteria.split(","));
        return new SortCriteria.Builder()
                .withIsAscending(isAscending)
                .withCriterias(criteriaList)
                .build();
    }
}
