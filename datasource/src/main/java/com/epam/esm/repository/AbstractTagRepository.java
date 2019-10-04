package com.epam.esm.repository;

import com.epam.esm.entity.Tag;


public interface AbstractTagRepository extends FindAllRepository<Tag>, FindOneRepository<Tag>, SaveRepository<Tag>,
        RemoveRepository, QueryRepository<Tag>  {
}
