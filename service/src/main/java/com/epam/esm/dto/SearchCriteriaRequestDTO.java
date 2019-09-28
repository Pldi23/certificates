package com.epam.esm.dto;

import java.util.Map;

/**
 * giftcertificates
 *
 * @author Dzmitry Platonov on 2019-09-27.
 * @version 0.0.1
 */
public class SearchCriteriaRequestDTO {

    private Map<String, String> parameters;

    public SearchCriteriaRequestDTO() {
    }

    public SearchCriteriaRequestDTO(Map<String, String> parameters) {
        this.parameters = parameters;
    }

    public Map<String, String> getParameters() {
        return parameters;
    }

    public void setParameters(Map<String, String> parameters) {
        this.parameters = parameters;
    }
}
