package com.epam.esm.converter;


import com.epam.esm.dto.SearchCriteriaRequestDTO;
import com.epam.esm.entity.criteria.SearchCriteria;
import org.springframework.stereotype.Component;

import java.util.Map;

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
