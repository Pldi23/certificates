package com.epam.esm.converter;


import com.epam.esm.dto.SearchCriteriaDTO;
import com.epam.esm.dto.SortCriteriaDTO;
import com.epam.esm.entity.criteria.SearchCriteria;
import com.epam.esm.entity.criteria.SortCriteria;
import org.springframework.stereotype.Component;

/**
 * giftcertificates
 *
 * @author Dzmitry Platonov on 2019-09-26.
 * @version 0.0.1
 */
@Component
public class CriteriaConverter {

    public SearchCriteria convertSearchCriteria(SearchCriteriaDTO searchCriteriaDTO) {
        return new SearchCriteria.Builder()
                .withIdCriteria(searchCriteriaDTO.getIdCriteria())
                .withNameCriteria(searchCriteriaDTO.getNameCriteria())
                .withDescriptionCriteria(searchCriteriaDTO.getDescriptionCriteria())
                .withCreationDateCriteria(searchCriteriaDTO.getCreationDateCriteria())
                .withModificationDateCriteria(searchCriteriaDTO.getModificationDateCriteria())
                .withExpirationDateCriteria(searchCriteriaDTO.getExpirationDateCriteria())
                .build();
    }

    public SortCriteria convertSortCriteria(SortCriteriaDTO sortCriteriaDTO) {
        return new SortCriteria.Builder()
                .withCriterias(sortCriteriaDTO.getCriteriaList())
                .withIsAscending(sortCriteriaDTO.isAscending())
                .withLimit(sortCriteriaDTO.getLimit())
                .withOffset(sortCriteriaDTO.getOffset())
                .build();
    }
}
