package com.epam.esm.entity.criteria;

import java.time.LocalDate;
import java.util.List;

public class CreationDateCriteria implements Criteria {

    private ParameterSearchType parameterSearchType;
    private List<LocalDate> criteriaList;

    public CreationDateCriteria(ParameterSearchType parameterSearchType, List<LocalDate> criteriaList) {
        this.parameterSearchType = parameterSearchType;
        this.criteriaList = criteriaList;
    }

    public ParameterSearchType getParameterSearchType() {
        return parameterSearchType;
    }

    public void setParameterSearchType(ParameterSearchType parameterSearchType) {
        this.parameterSearchType = parameterSearchType;
    }

    public List<LocalDate> getCriteriaList() {
        return criteriaList;
    }

    public void setCriteriaList(List<LocalDate> criteriaList) {
        this.criteriaList = criteriaList;
    }
}
