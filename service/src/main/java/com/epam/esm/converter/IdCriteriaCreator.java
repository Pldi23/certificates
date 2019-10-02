package com.epam.esm.converter;

import com.epam.esm.entity.criteria.IdCriteria;
import com.epam.esm.entity.criteria.ParameterSearchType;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * giftcertificates
 *
 * @author Dzmitry Platonov on 2019-10-02.
 * @version 0.0.1
 */
public class IdCriteriaCreator implements CriteriaCreator<IdCriteria> {
    @Override
    public IdCriteria create(Map<String, String> criteriaMap, String criteriaKey) {
//        parameters = buildParameters(criteriaString);
//        parameterSearchType = (ParameterSearchType) parameters.get(0);
//        criteriaString = String.valueOf(parameters.get(1));
//        List<Long> criteriaIdList = Arrays.stream(criteriaString.split(","))
//                .map(Long::parseLong).collect(Collectors.toList());
//        criteria = new IdCriteria(parameterSearchType, criteriaIdList);
        return null;
    }
}
