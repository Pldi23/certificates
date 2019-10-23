package com.epam.esm.entity.criteria;

import java.util.List;


public class IdCriteria implements Criteria {

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
