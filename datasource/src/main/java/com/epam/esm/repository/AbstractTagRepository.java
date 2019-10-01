package com.epam.esm.repository;

import com.epam.esm.entity.Tag;

import java.util.List;

/**
 * gift certificates
 *
 * @author Dzmitry Platonov on 2019-09-29.
 * @version 0.0.1
 */
public interface AbstractTagRepository extends Repository<Tag> {

    void removeById(long id);
    List<Tag> getTagsByCertificate(long id);
}
