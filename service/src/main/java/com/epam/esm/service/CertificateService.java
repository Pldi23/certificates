package com.epam.esm.service;

import com.epam.esm.dto.CertificatePatchDTO;
import com.epam.esm.dto.GiftCertificateDTO;
import com.epam.esm.dto.PageAndSortDTO;
import com.epam.esm.dto.SearchCriteriaRequestDTO;

import java.util.List;

/**
 * to encapsulate service operations around {@link GiftCertificateDTO}
 *
 * @author Dzmitry Platonov on 2019-10-01.
 * @version 0.0.1
 */
public interface CertificateService extends FindAllService<GiftCertificateDTO>, FindOneService<GiftCertificateDTO>,
        SaveService<GiftCertificateDTO>, UpdateService<GiftCertificateDTO>, DeleteService, PatchService<GiftCertificateDTO, CertificatePatchDTO> {

    List<GiftCertificateDTO> findByCriteria(SearchCriteriaRequestDTO searchCriteriaDTO, PageAndSortDTO pageAndSortDTO);

    List<GiftCertificateDTO> getByTag(long id, PageAndSortDTO pageAndSortDTO);

    List<GiftCertificateDTO> findByOrder(Long orderId, PageAndSortDTO pageAndSortDTO);

}
