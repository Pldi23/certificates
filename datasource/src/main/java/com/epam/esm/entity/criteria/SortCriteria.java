package com.epam.esm.entity.criteria;

import java.util.List;

/**
 * giftcertificates
 *
 * @author Dzmitry Platonov on 2019-09-26.
 * @version 0.0.1
 */
public class SortCriteria {

    private boolean isPrimaryAscending;
    private List<String> criteriaList;

    private SortCriteria() {
    }

    public static class Builder {
        private SortCriteria sortCriteria;

        public Builder() {
            sortCriteria = new SortCriteria();
        }

        public Builder withIsAscending(boolean isAscending) {
            sortCriteria.isPrimaryAscending = isAscending;
            return this;
        }

        public Builder withCriterias(List<String> criterias) {
            sortCriteria.criteriaList = criterias;
            return this;
        }

        public SortCriteria build() {
            return sortCriteria;
        }
    }

    public boolean isPrimaryAscending() {
        return isPrimaryAscending;
    }

    public List<String> getCriteriaList() {
        return criteriaList;
    }
}
