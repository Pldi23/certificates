package com.epam.esm.converter;

import com.epam.esm.entity.criteria.NameCriteria;
import com.epam.esm.entity.criteria.TextSearchType;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
import java.util.Map;


@Component
public class NameCriteriaCreator implements CriteriaCreator<NameCriteria> {

    private CriteriaCreatorHelper<NameCriteria> helper;

    public NameCriteriaCreator(CriteriaCreatorHelper<NameCriteria> helper) {
        this.helper = helper;
    }

    @Override
    public NameCriteria create(Map<String, String> criteriaMap) {
        String requestParameter = criteriaMap.get(ConverterConstant.NAME);
        if (requestParameter == null || requestParameter.isBlank()) {
            return null;
        }
        TextSearchType textSearchType = helper.buildTextSearchType(requestParameter);
        requestParameter = helper.cutPrefixCriteriaString(requestParameter);
        List<String> criteriaNameList = Arrays.asList(requestParameter.split(","));
        return new NameCriteria(textSearchType, criteriaNameList);
    }
}
