package com.epam.esm.dto;

import java.util.List;

/**
 * gift certificates
 *
 * @author Dzmitry Platonov on 2019-09-26.
 * @version 0.0.1
 */
public class SortCriteriaDTO {

    private boolean isAscending;
    private List<String> criteriaList;
    private int limit;
    private long offset;

    private SortCriteriaDTO() {
    }

    public static class Builder {
        private SortCriteriaDTO sortCriteriaDTO;

        public Builder() {
            sortCriteriaDTO = new SortCriteriaDTO();
        }

        public Builder withIsAscending(boolean isAscending) {
            sortCriteriaDTO.isAscending = isAscending;
            return this;
        }

        public Builder withCriterias(List<String> criterias) {
            sortCriteriaDTO.criteriaList = criterias;
            return this;
        }

        public Builder withLimit(int limit) {
            sortCriteriaDTO.limit = limit;
            return this;
        }

        public Builder withOffset(long offset) {
            sortCriteriaDTO.offset = offset;
            return this;
        }

        public SortCriteriaDTO build() {
            return sortCriteriaDTO;
        }
    }

    public boolean isAscending() {
        return isAscending;
    }

    public List<String> getCriteriaList() {
        return criteriaList;
    }

    public int getLimit() {
        return limit;
    }

    public long getOffset() {
        return offset;
    }
}
