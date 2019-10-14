package com.epam.esm.repository.hibernate;

import com.epam.esm.entity.Tag;

import java.util.List;
import java.util.Optional;


public interface AbstractTagRepository extends FindAllRepository<Tag>, FindOneRepository<Tag>, SaveRepository<Tag>,
        RemoveRepository, QueryRepository<Tag>  {

    List<Tag> findTagsByCertificate(long certificateId);
    Optional<Tag> findByTitle(String title);
}
