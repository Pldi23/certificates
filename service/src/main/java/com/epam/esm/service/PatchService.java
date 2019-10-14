package com.epam.esm.service;

import com.epam.esm.dto.CertificatePatchDTO;

import java.util.Optional;

/**
 * gift-certificates
 *
 * @author Dzmitry Platonov on 2019-10-12.
 * @version 0.0.1
 */
public interface PatchService<T, S> {

    T patch(S s, Long id);
}
