package com.epam.esm.dto;

import java.util.Map;

/**
 * giftcertificates
 *
 * @author Dzmitry Platonov on 2019-09-27.
 * @version 0.0.1
 */
public class SortCriteriaRequestDTO {

    private Map<String, String> parameters;

    public SortCriteriaRequestDTO() {
    }

    public SortCriteriaRequestDTO(Map<String, String> params) {
        this.parameters = params;
    }

    public Map<String, String> getParameters() {
        return parameters;
    }

    public void setParameters(Map<String, String> params) {
        this.parameters = params;
    }
}
