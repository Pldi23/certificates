package com.epam.esm.converter;

import com.epam.esm.entity.criteria.ModificationDateCriteria;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class ModificationDateCriteriaCreator implements CriteriaCreator<ModificationDateCriteria> {

    private CriteriaCreatorHelper<ModificationDateCriteria> helper;

    public ModificationDateCriteriaCreator(CriteriaCreatorHelper<ModificationDateCriteria> helper) {
        this.helper = helper;
    }

    @Override
    public ModificationDateCriteria create(Map<String, String> criteriaMap) {
        return helper.createDateCriteria(criteriaMap, ConverterConstant.MODIFICATION_DATE,
                ModificationDateCriteria::new);
    }
}
