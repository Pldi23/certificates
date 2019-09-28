package com.epam.esm.entity.criteria;

import java.util.List;

/**
 * giftcertificates
 *
 * @author Dzmitry Platonov on 2019-09-26.
 * @version 0.0.1
 */
public class DescriptionCriteria implements Criteria {

    private TextSearchType searchType;
    private List<String> criteriaList;

    public DescriptionCriteria(TextSearchType searchType, List<String> criteriaList) {
        this.searchType = searchType;
        this.criteriaList = criteriaList;
    }

    public TextSearchType getSearchType() {
        return searchType;
    }

    public void setSearchType(TextSearchType searchType) {
        this.searchType = searchType;
    }

    public List<String> getCriteriaList() {
        return criteriaList;
    }

    public void setCriteriaList(List<String> criteriaList) {
        this.criteriaList = criteriaList;
    }
}
