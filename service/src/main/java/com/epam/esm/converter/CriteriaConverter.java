package com.epam.esm.converter;


import com.epam.esm.dto.LimitOffsetCriteriaRequestDTO;
import com.epam.esm.dto.SearchCriteriaRequestDTO;
import com.epam.esm.dto.SortCriteriaRequestDTO;
import com.epam.esm.entity.criteria.LimitOffsetCriteria;
import com.epam.esm.entity.criteria.SearchCriteria;
import com.epam.esm.entity.criteria.SortCriteria;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static com.epam.esm.converter.ConverterConstant.LIMIT;
import static com.epam.esm.converter.ConverterConstant.OFFSET;
import static com.epam.esm.converter.ConverterConstant.SORT;
import static com.epam.esm.converter.ConverterConstant.SORT_DESCENDING_KEY;

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
}
