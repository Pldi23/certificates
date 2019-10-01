package com.epam.esm.service;

import com.epam.esm.dto.TagDTO;

import java.util.List;
import java.util.Optional;

/**
 * giftcertificates
 *
 * @author Dzmitry Platonov on 2019-10-01.
 * @version 0.0.1
 */
public interface TagService {

    List<TagDTO> getTag(long id);
    List<TagDTO> findAll();
    Optional<TagDTO> save(TagDTO tagDTO);
    List<TagDTO> getTagsByCertificate(long id);
    void delete(long id);
}
