package com.epam.esm.converter;

import com.epam.esm.entity.criteria.DescriptionCriteria;
import com.epam.esm.entity.criteria.TextSearchType;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Component
public class DescriptionCriteriaCreator implements CriteriaCreator<DescriptionCriteria> {

    private CriteriaCreatorHelper helper;

    public DescriptionCriteriaCreator(CriteriaCreatorHelper helper) {
        this.helper = helper;
    }

    @Override
    public DescriptionCriteria create(Map<String, String> criteriaMap) {
        String requestParameter = criteriaMap.get(ConverterConstant.DESCRIPTION);
        if (requestParameter == null || requestParameter.isBlank()) {
            return null;
        }
        TextSearchType textSearchType = helper.buildTextSearchType(requestParameter);
        requestParameter = helper.cutPrefixCriteriaString(requestParameter);
        List<String> criteriaNameList = Arrays.asList(requestParameter.split(","));
        return new DescriptionCriteria(textSearchType, criteriaNameList);
    }
}
