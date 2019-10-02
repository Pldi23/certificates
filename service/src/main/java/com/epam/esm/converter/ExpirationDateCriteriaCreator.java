package com.epam.esm.converter;

import com.epam.esm.entity.criteria.ExpirationDateCriteria;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class ExpirationDateCriteriaCreator implements CriteriaCreator<ExpirationDateCriteria> {

    private CriteriaCreatorHelper<ExpirationDateCriteria> helper;

    public ExpirationDateCriteriaCreator(CriteriaCreatorHelper<ExpirationDateCriteria> helper) {
        this.helper = helper;
    }

    @Override
    public ExpirationDateCriteria create(Map<String, String> criteriaMap) {
        return helper.createDateCriteria(criteriaMap, ConverterConstant.EXPIRATION_DATE,
                ExpirationDateCriteria::new);
    }
}
