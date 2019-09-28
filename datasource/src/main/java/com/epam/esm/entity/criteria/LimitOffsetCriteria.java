package com.epam.esm.entity.criteria;


/**
 * gift certificates
 *
 * @author Dzmitry Platonov on 2019-09-27.
 * @version 0.0.1
 */
public class LimitOffsetCriteria {

    private int limit;
    private long offset;

    private LimitOffsetCriteria() {
    }

    public static class Builder {
        private LimitOffsetCriteria limitOffsetCriteria;

        public Builder() {
            limitOffsetCriteria = new LimitOffsetCriteria();
        }

        public Builder withLimit(int limit) {
            limitOffsetCriteria.limit = limit;
            return this;
        }

        public Builder withOffset(long offset) {
            limitOffsetCriteria.offset = offset;
            return this;
        }

        public LimitOffsetCriteria build() {
            return limitOffsetCriteria;
        }
    }

    public int getLimit() {
        return limit;
    }

    public long getOffset() {
        return offset;
    }
}
