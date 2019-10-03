package com.epam.esm.service;

import com.epam.esm.dto.GiftCertificateDTO;

/**
 * to encapsulate service operations around {@link GiftCertificateDTO}
 *
 * @author Dzmitry Platonov on 2019-10-01.
 * @version 0.0.1
 */
public interface CertificateService extends FindAllService<GiftCertificateDTO>, FindOneService<GiftCertificateDTO>,
        SaveService<GiftCertificateDTO>, UpdateService<GiftCertificateDTO>, DeleteService,
        FindCertificatesByTagService, FindCertificatesByCriteriaService {

}
