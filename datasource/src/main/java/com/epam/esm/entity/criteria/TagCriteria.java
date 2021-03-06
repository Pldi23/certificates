package com.epam.esm.entity.criteria;

import java.util.List;


public class TagCriteria implements Criteria {

    private ParameterSearchType parameterSearchType;
    private List<Long> tagIds;

    public TagCriteria(ParameterSearchType parameterSearchType, List<Long> tagIds) {
        this.parameterSearchType = parameterSearchType;
        this.tagIds = tagIds;
    }

    public ParameterSearchType getParameterSearchType() {
        return parameterSearchType;
    }

    public void setParameterSearchType(ParameterSearchType parameterSearchType) {
        this.parameterSearchType = parameterSearchType;
    }

    public List<Long> getTagIds() {
        return tagIds;
    }

    public void setTagIds(List<Long> tagIds) {
        this.tagIds = tagIds;
    }
}
