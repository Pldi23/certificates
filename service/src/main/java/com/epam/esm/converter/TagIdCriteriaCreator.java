package com.epam.esm.converter;

import com.epam.esm.entity.criteria.ParameterSearchType;
import com.epam.esm.entity.criteria.TagCriteria;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@Component
public class TagIdCriteriaCreator implements CriteriaCreator<TagCriteria> {

    private CriteriaCreatorHelper<TagCriteria> helper;

    public TagIdCriteriaCreator(CriteriaCreatorHelper<TagCriteria> helper) {
        this.helper = helper;
    }

    @Override
    public TagCriteria create(Map<String, String> criteriaMap) {
        String requestParameter = criteriaMap.get(ConverterConstant.TAG_ID);
        if (requestParameter == null || requestParameter.isBlank()) {
            return null;
        }
        List<Object> params = helper.buildParameters(requestParameter);
        requestParameter = String.valueOf(params.get(1));
        List<Long> criteriaTagList = Arrays.stream(requestParameter.split(","))
                .map(Long::parseLong).collect(Collectors.toList());
        ParameterSearchType parameterSearchType = (ParameterSearchType) params.get(0);
        return new TagCriteria(parameterSearchType, criteriaTagList);
    }
}
