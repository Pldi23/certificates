package com.epam.esm.entity.criteria;

import java.util.List;

/**
 * giftcertificates
 *
 * @author Dzmitry Platonov on 2019-09-26.
 * @version 0.0.1
 */
public class IdCriteria {

    private ParameterSearchType parameterSearchType;
    private List<Long> criteriaList;

    public IdCriteria(ParameterSearchType parameterSearchType, List<Long> criteriaList) {
        this.parameterSearchType = parameterSearchType;
        this.criteriaList = criteriaList;
    }

    public ParameterSearchType getParameterSearchType() {
        return parameterSearchType;
    }

    public void setParameterSearchType(ParameterSearchType parameterSearchType) {
        this.parameterSearchType = parameterSearchType;
    }

    public List<Long> getCriteriaList() {
        return criteriaList;
    }

    public void setCriteriaList(List<Long> criteriaList) {
        this.criteriaList = criteriaList;
    }


}
