package com.epam.esm.entity.criteria;

import java.util.List;

public class TagNameCriteria implements Criteria {

    private TextSearchType textSearchType;
    private List<String> tagNames;

    public TagNameCriteria(TextSearchType textSearchType, List<String> tagNames) {
        this.textSearchType = textSearchType;
        this.tagNames = tagNames;
    }

    public TextSearchType getTextSearchType() {
        return textSearchType;
    }

    public void setTextSearchType(TextSearchType textSearchType) {
        this.textSearchType = textSearchType;
    }

    public List<String> getTagNames() {
        return tagNames;
    }

    public void setTagNames(List<String> tagNames) {
        this.tagNames = tagNames;
    }
}
