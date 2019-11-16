package com.epam.esm.parser;

import com.epam.esm.dto.LimitOffsetCriteriaRequestDTO;
import com.epam.esm.dto.OrderSearchCriteriaDTO;
import com.epam.esm.dto.PageAndSortDTO;
import com.epam.esm.dto.SearchCriteriaRequestDTO;
import com.epam.esm.dto.SortCriteriaRequestDTO;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

import static com.epam.esm.constant.RequestConstant.CERTIFICATE_ID;
import static com.epam.esm.constant.RequestConstant.CERTIFICATE_NAME;
import static com.epam.esm.constant.RequestConstant.EMAIL;
import static com.epam.esm.constant.RequestConstant.LIMIT_PARAMETER;
import static com.epam.esm.constant.RequestConstant.OFFSET_PARAMETER;
import static com.epam.esm.constant.RequestConstant.PAGE_PARAMETER;
import static com.epam.esm.constant.RequestConstant.SIZE_PARAMETER;
import static com.epam.esm.constant.RequestConstant.SORT_PARAMETER;
import static com.epam.esm.constant.RequestConstant.USER_ID;

/**
 * request to sort dtos parser
 *
 * @author Dzmitry Platonov on 2019-09-27.
 * @version 0.0.1
 */
@Component
@PropertySource("app.properties")
@Log4j2
public class DtoParser {

    @Value("${default.size}")
    private String defaultSize;
    @Value("${default.page}")
    private String defaultPage;

    public SearchCriteriaRequestDTO parseSearchCriteria(Map<String, String> requestParams) {
        requestParams.forEach((k,v) -> log.info(k + " : " + v));

        SearchCriteriaRequestDTO searchCriteriaRequestDTO = new SearchCriteriaRequestDTO();
        searchCriteriaRequestDTO.setParameters(requestParams.entrySet().stream()
                .filter(entry -> !entry.getKey().equals(SORT_PARAMETER)
                        && !entry.getValue().equals(LIMIT_PARAMETER)
                        && !entry.getKey().equals(OFFSET_PARAMETER))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue)));
        log.info("here " + searchCriteriaRequestDTO.getParameters().size());
        searchCriteriaRequestDTO.getParameters().forEach((k,v) -> log.info(k + " : " + v));
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
                        Integer.parseInt(requestParams.get(PAGE_PARAMETER)) : Integer.parseInt(defaultPage))
                .size(requestParams.containsKey(SIZE_PARAMETER) && !requestParams.get(SIZE_PARAMETER).isBlank() ?
                        Integer.parseInt(requestParams.get(SIZE_PARAMETER)) : Integer.parseInt(defaultSize))
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
