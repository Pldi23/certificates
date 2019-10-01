package com.epam.esm.parser;

import com.epam.esm.dto.*;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.stream.Collectors;
import static com.epam.esm.constant.RequestConstant.*;

/**
 * gift certificates
 *
 * @author Dzmitry Platonov on 2019-09-27.
 * @version 0.0.1
 */
@Component
public class DtoParser {

    public SearchCriteriaRequestDTO parseSearchCriteria(Map<String, String> requestParams) {
        SearchCriteriaRequestDTO searchCriteriaRequestDTO = new SearchCriteriaRequestDTO();
        searchCriteriaRequestDTO.setParameters(requestParams.entrySet().stream()
                .takeWhile(entry -> !entry.getKey().equals(SORT_PARAMETER)
                        && !entry.getValue().equals(LIMIT_PARAMETER)
                        && !entry.getKey().equals(OFFSET_PARAMETER))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue)));
        return searchCriteriaRequestDTO;
    }

    public SortCriteriaRequestDTO parseSortCriteria(Map<String, String> requestParams) {
        SortCriteriaRequestDTO sortCriteriaRequestDTO = new SortCriteriaRequestDTO();
        sortCriteriaRequestDTO.setParameters(requestParams.entrySet().stream()
                .filter(entry -> entry.getKey().equals(SORT_PARAMETER))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue)));

        return sortCriteriaRequestDTO;
    }

    public LimitOffsetCriteriaRequestDTO parseLimitOffsetCriteria(Map<String, String> requestParams) {
        LimitOffsetCriteriaRequestDTO limitOffsetCriteriaRequestDTO = new LimitOffsetCriteriaRequestDTO();
        limitOffsetCriteriaRequestDTO.setParameters(requestParams.entrySet().stream()
                .filter(entry -> entry.getKey().equals(LIMIT_PARAMETER) || entry.getKey().equals(OFFSET_PARAMETER))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue)));

        return limitOffsetCriteriaRequestDTO;
    }
}
