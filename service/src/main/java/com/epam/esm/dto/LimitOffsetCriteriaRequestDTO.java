package com.epam.esm.dto;

import java.util.Map;

/**
 * gift certificates
 *
 * @author Dzmitry Platonov on 2019-09-27.
 * @version 0.0.1
 */
public class LimitOffsetCriteriaRequestDTO {

    private Map<String, String> parameters;

    public LimitOffsetCriteriaRequestDTO() {
    }

    public LimitOffsetCriteriaRequestDTO(Map<String, String> parameters) {
        this.parameters = parameters;
    }

    public Map<String, String> getParameters() {
        return parameters;
    }

    public void setParameters(Map<String, String> parameters) {
        this.parameters = parameters;
    }
}
