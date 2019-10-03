package com.epam.esm.service;

import com.epam.esm.dto.GiftCertificateDTO;

import java.util.List;

/**
 * to find {@link GiftCertificateDTO} by id
 *
 * @author Dzmitry Platonov on 2019-10-03.
 * @version 0.0.1
 */
public interface FindCertificatesByTagService {

    List<GiftCertificateDTO> getByTag(long id);
}
