package com.epam.esm.controller.parser;

import com.epam.esm.dto.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.stream.Collectors;

/**
 * gift certificates
 *
 * @author Dzmitry Platonov on 2019-09-27.
 * @version 0.0.1
 */
@Component
public class DtoParser {

    private static final Logger log = LogManager.getLogger();

    private static final String SORT_PARAMETER = "sort";
    private static final String LIMIT_PARAMETER = "limit";
    private static final String OFFSET_PARAMETER = "offset";


    public SearchCriteriaRequestDTO parseSearchCriteria(Map<String, String> requestParams) {
        SearchCriteriaRequestDTO searchCriteriaRequestDTO = new SearchCriteriaRequestDTO();
        searchCriteriaRequestDTO.setParameters(requestParams.entrySet().stream()
                .takeWhile(entry -> !entry.getKey().equals(SORT_PARAMETER)
                        && !entry.getValue().equals(LIMIT_PARAMETER)
                        && !entry.getKey().equals(OFFSET_PARAMETER))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue)));
        log.debug("search criteria");
        searchCriteriaRequestDTO.getParameters().forEach((k, v) -> log.debug("key: " + k + " : value is: " + v));
        return searchCriteriaRequestDTO;
    }

    public SortCriteriaRequestDTO parseSortCriteria(Map<String, String> requestParams) {
        SortCriteriaRequestDTO sortCriteriaRequestDTO = new SortCriteriaRequestDTO();
        sortCriteriaRequestDTO.setParameters(requestParams.entrySet().stream()
                .filter(entry -> entry.getKey().equals(SORT_PARAMETER))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue)));

        log.debug("sort criteria");
        sortCriteriaRequestDTO.getParameters().forEach((k, v) -> log.debug("key: " + k + " : value is: " + v));
        return sortCriteriaRequestDTO;
    }

    public LimitOffsetCriteriaRequestDTO parseLimitOffsetCriteria(Map<String, String> requestParams) {
        LimitOffsetCriteriaRequestDTO limitOffsetCriteriaRequestDTO = new LimitOffsetCriteriaRequestDTO();
        limitOffsetCriteriaRequestDTO.setParameters(requestParams.entrySet().stream()
                .filter(entry -> entry.getKey().equals(LIMIT_PARAMETER) || entry.getKey().equals(OFFSET_PARAMETER))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue)));

        log.debug("limit offset criteria");
        limitOffsetCriteriaRequestDTO.getParameters().forEach((k, v) -> log.debug("key: " + k + " : value is: " + v));
        return limitOffsetCriteriaRequestDTO;
    }
}
