package com.epam.esm.parser;

import com.epam.esm.dto.*;
import org.springframework.stereotype.Component;

import java.util.Arrays;
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

    public PageAndSortDTO parsePageAndSortCriteria(Map<String, String> requestParams) {
        return PageAndSortDTO.builder()
                .sortParameter(requestParams.getOrDefault(SORT_PARAMETER, null))
                .page(requestParams.containsKey(PAGE_PARAMETER) && !requestParams.get(PAGE_PARAMETER).isBlank() ?
                        Integer.parseInt(requestParams.get(PAGE_PARAMETER)) : 1)
                .size(requestParams.containsKey(SIZE_PARAMETER) && !requestParams.get(SIZE_PARAMETER).isBlank() ?
                        Integer.parseInt(requestParams.get(SIZE_PARAMETER)) : Integer.MAX_VALUE)
                .build();
    }

    public OrderSearchCriteriaDTO parseOrderSearchDTO(Map<String, String> requestParams) {
        return OrderSearchCriteriaDTO.builder()
                .email(requestParams.getOrDefault(EMAIL, null))
                .userId(requestParams.containsKey(USER_ID) ? Long.parseLong(requestParams.get(USER_ID)) : null)
                .certificatesNames(requestParams.containsKey(CERTIFICATE_NAME) ? Arrays.asList(requestParams.get(CERTIFICATE_NAME).split(",")) : null)
                .certificatesIds(requestParams.containsKey(CERTIFICATE_ID) ? Arrays.stream(requestParams.get(CERTIFICATE_ID).split(","))
                        .map(Long::parseLong)
                        .collect(Collectors.toList()) : null)
                .build();
    }
}
