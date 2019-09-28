package com.epam.esm.entity.criteria;

import java.math.BigDecimal;
import java.util.List;

/**
 * gift certificates
 *
 * @author Dzmitry Platonov on 2019-09-27.
 * @version 0.0.1
 */
public class PriceCriteria implements Criteria {

    private ParameterSearchType parameterSearchType;
    private List<BigDecimal> criteriaList;

    public PriceCriteria(ParameterSearchType parameterSearchType, List<BigDecimal> criteriaList) {
        this.parameterSearchType = parameterSearchType;
        this.criteriaList = criteriaList;
    }

    public ParameterSearchType getParameterSearchType() {
        return parameterSearchType;
    }

    public void setParameterSearchType(ParameterSearchType parameterSearchType) {
        this.parameterSearchType = parameterSearchType;
    }

    public List<BigDecimal> getCriteriaList() {
        return criteriaList;
    }

    public void setCriteriaList(List<BigDecimal> criteriaList) {
        this.criteriaList = criteriaList;
    }
}
