package com.epam.esm.converter;

import com.epam.esm.entity.criteria.TagNameCriteria;
import com.epam.esm.entity.criteria.TextSearchType;
import lombok.extern.log4j.Log4j2;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Log4j2
public class TagNameCriteriaCreator implements CriteriaCreator<TagNameCriteria> {

    private CriteriaCreatorHelper<TagNameCriteria> helper;

    public TagNameCriteriaCreator(CriteriaCreatorHelper<TagNameCriteria> helper) {
        this.helper = helper;
    }

    @Override
    public TagNameCriteria create(Map<String, String> criteriaMap) {
        String requestParameter = criteriaMap.get(ConverterConstant.TAG_NAME);
        if (requestParameter == null || requestParameter.isBlank()) {
            return null;
        }
        TextSearchType textSearchType = helper.buildTextSearchType(requestParameter);
        requestParameter = helper.cutPrefixCriteriaString(requestParameter);
        List<String> criteriaTagNameList = Arrays.asList(requestParameter.split(","));
        return new TagNameCriteria(textSearchType, criteriaTagNameList);
    }
}
