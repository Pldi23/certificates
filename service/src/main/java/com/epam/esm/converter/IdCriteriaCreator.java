package com.epam.esm.converter;

import com.epam.esm.entity.criteria.IdCriteria;
import com.epam.esm.entity.criteria.ParameterSearchType;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class IdCriteriaCreator implements CriteriaCreator<IdCriteria> {

    private CriteriaCreatorHelper<IdCriteria> helper;

    IdCriteriaCreator(CriteriaCreatorHelper<IdCriteria> helper) {
        this.helper = helper;
    }

    @Override
    public IdCriteria create(Map<String, String> criteriaMap) {
        String requestParameter = criteriaMap.get(ConverterConstant.ID);
        if (requestParameter == null || requestParameter.isBlank()) {
            return null;
        }
        List<Object> parameters = helper.buildParameters(requestParameter);
        ParameterSearchType parameterSearchType = (ParameterSearchType) parameters.get(0);
        requestParameter = String.valueOf(parameters.get(1));
        List<Long> criteriaIdList = Arrays.stream(requestParameter.split(","))
                .map(Long::parseLong).collect(Collectors.toList());
        return new IdCriteria(parameterSearchType, criteriaIdList);
    }
}
