package com.epam.esm.repository;

import com.epam.esm.entity.Tag;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;


public interface AbstractTagRepository extends FindAllSpecifiedRepository<Tag>, FindOneRepository<Tag>, SaveRepository<Tag>,
        RemoveRepository {

    Optional<Tag> findByTitle(String title);
    List<Tag> findMostCostEffectiveTagByUser(long userId);
    BigDecimal getTagCost(long id);
}
