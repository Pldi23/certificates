package com.epam.esm.converter;

import com.epam.esm.entity.criteria.Criteria;

import java.util.Map;

public interface CriteriaCreator<T extends Criteria> {

    T create(Map<String, String> criteriaMap);
}
