package com.epam.esm.converter;


import com.epam.esm.dto.*;
import com.epam.esm.entity.criteria.*;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static com.epam.esm.converter.ConverterConstant.*;

@Component
public class CriteriaConverter {

    private CriteriaCreatorHelper helper;

    public CriteriaConverter(CriteriaCreatorHelper helper) {
        this.helper = helper;
    }

    public SearchCriteria convertSearchCriteria(SearchCriteriaRequestDTO searchCriteriaRequestDTO) {

        Map<String, String> criteriaMap = searchCriteriaRequestDTO.getParameters();

        return new SearchCriteria.Builder()
                .withIdCriteria(new IdCriteriaCreator(helper).create(criteriaMap))
                .withNameCriteria(new NameCriteriaCreator(helper).create(criteriaMap))
                .withDescriptionCriteria(new DescriptionCriteriaCreator(helper).create(criteriaMap))
                .withPriceCriteria(new PriceCriteriaCreator(helper).create(criteriaMap))
                .withCreationDateCriteria(new CreationDateCriteriaCreator(helper).create(criteriaMap))
                .withExpirationDateCriteria(new ExpirationDateCriteriaCreator(helper).create(criteriaMap))
                .withModificationDateCriteria(new ModificationDateCriteriaCreator(helper).create(criteriaMap))
                .withTagCriteria(new TagIdCriteriaCreator(helper).create(criteriaMap))
                .withTagNameCriteria(new TagNameCriteriaCreator(helper).create(criteriaMap))
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
