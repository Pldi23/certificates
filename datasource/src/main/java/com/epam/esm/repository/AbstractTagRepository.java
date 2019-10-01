package com.epam.esm.repository;

import com.epam.esm.entity.Tag;

import java.util.List;

public interface AbstractTagRepository extends Repository<Tag> {

    void removeById(long id);
    List<Tag> getTagsByCertificate(long id);
}
