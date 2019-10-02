package com.epam.esm.converter;

import com.epam.esm.entity.criteria.Criteria;

import java.util.Map;

/**
 * used to create {@link Criteria} from map of request parameters
 *
 * @author Dzmitry Platonov on 2019-10-02.
 * @version 0.0.1
 */
public interface CriteriaCreator<T extends Criteria> {

    T create(Map<String, String> criteriaMap, String criteriaKey);
}
