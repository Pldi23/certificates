package com.epam.esm.converter;

import com.epam.esm.entity.criteria.ParameterSearchType;
import com.epam.esm.entity.criteria.PriceCriteria;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@Component
public class PriceCriteriaCreator implements CriteriaCreator<PriceCriteria> {

    private CriteriaCreatorHelper<PriceCriteria> helper;

    public PriceCriteriaCreator(CriteriaCreatorHelper<PriceCriteria> helper) {
        this.helper = helper;
    }

    @Override
    public PriceCriteria create(Map<String, String> criteriaMap) {
        String requestParameter = criteriaMap.get(ConverterConstant.PRICE);
        if (requestParameter == null || requestParameter.isBlank()) {
            return null;
        }
        List<Object> parameters = helper.buildParameters(requestParameter);
        ParameterSearchType parameterSearchType = (ParameterSearchType) parameters.get(0);
        requestParameter = String.valueOf(parameters.get(1));
        List<BigDecimal> criteriaList = Arrays.stream(requestParameter.split(","))
                .map(BigDecimal::new).collect(Collectors.toList());
        return new PriceCriteria(parameterSearchType, criteriaList);
    }
}
