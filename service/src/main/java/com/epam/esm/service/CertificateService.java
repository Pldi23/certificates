package com.epam.esm.service;

import com.epam.esm.dto.GiftCertificateDTO;
import com.epam.esm.dto.LimitOffsetCriteriaRequestDTO;
import com.epam.esm.dto.SearchCriteriaRequestDTO;
import com.epam.esm.dto.SortCriteriaRequestDTO;

import java.util.List;
import java.util.Optional;

/**
 * giftcertificates
 *
 * @author Dzmitry Platonov on 2019-10-01.
 * @version 0.0.1
 */
public interface CertificateService {

    List<GiftCertificateDTO> findAll();
    Optional<GiftCertificateDTO> findOne(long id);
    Optional<GiftCertificateDTO> save(GiftCertificateDTO giftCertificateDTO);
    boolean update(GiftCertificateDTO giftCertificateDTO, long id);
    void delete(long id);
    List<GiftCertificateDTO> getByTag(long id);

    List<GiftCertificateDTO> findByCriteria(SearchCriteriaRequestDTO searchCriteriaDTO,
                                            SortCriteriaRequestDTO sortCriteriaDTO,
                                            LimitOffsetCriteriaRequestDTO limitOffsetCriteriaRequestDTO);
}
