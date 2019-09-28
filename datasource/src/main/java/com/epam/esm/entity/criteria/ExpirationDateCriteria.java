package com.epam.esm.entity.criteria;

import java.time.LocalDate;
import java.util.List;

/**
 * giftcertificates
 *
 * @author Dzmitry Platonov on 2019-09-26.
 * @version 0.0.1
 */
public class ExpirationDateCriteria implements Criteria {

    private ParameterSearchType parameterSearchType;
    private List<LocalDate> criteriaList;

    public ExpirationDateCriteria(ParameterSearchType parameterSearchType, List<LocalDate> criteriaList) {
        this.parameterSearchType = parameterSearchType;
        this.criteriaList = criteriaList;
    }

    public ParameterSearchType getSearchType() {
        return parameterSearchType;
    }

    public void setSearchType(ParameterSearchType parameterSearchType) {
        this.parameterSearchType = parameterSearchType;
    }

    public List<LocalDate> getCriteriaList() {
        return criteriaList;
    }

    public void setCriteriaList(List<LocalDate> criteriaList) {
        this.criteriaList = criteriaList;
    }
}
