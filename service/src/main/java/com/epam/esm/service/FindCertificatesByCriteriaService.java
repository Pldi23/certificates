package com.epam.esm.service;

import com.epam.esm.dto.GiftCertificateDTO;
import com.epam.esm.dto.LimitOffsetCriteriaRequestDTO;
import com.epam.esm.dto.SearchCriteriaRequestDTO;
import com.epam.esm.dto.SortCriteriaRequestDTO;

import java.util.List;

/**
 * to find {@link GiftCertificateDTO} by criteria
 *
 * @author Dzmitry Platonov on 2019-10-03.
 * @version 0.0.1
 */
public interface FindCertificatesByCriteriaService {

    List<GiftCertificateDTO> findByCriteria(SearchCriteriaRequestDTO searchCriteriaDTO,
                                            SortCriteriaRequestDTO sortCriteriaDTO,
                                            LimitOffsetCriteriaRequestDTO limitOffsetCriteriaRequestDTO);
}
