package com.epam.esm.converter;

import com.epam.esm.entity.criteria.CreationDateCriteria;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class CreationDateCriteriaCreator implements CriteriaCreator<CreationDateCriteria> {

    private CriteriaCreatorHelper<CreationDateCriteria> helper;

    CreationDateCriteriaCreator(CriteriaCreatorHelper<CreationDateCriteria> helper) {
        this.helper = helper;
    }

    @Override
    public CreationDateCriteria create(Map<String, String> criteriaMap) {
        return helper.createDateCriteria(criteriaMap, ConverterConstant.CREATION_DATE,
                CreationDateCriteria::new);
    }
}
