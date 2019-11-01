package com.epam.esm.repository;

import com.epam.esm.entity.Tag;
import com.epam.esm.repository.predicate.Specification;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;


public interface AbstractTagRepository extends FindAllSpecifiedRepository<Tag>, FindOneRepository<Tag>, SaveRepository<Tag>,
        RemoveRepository {

    Optional<Tag> findByTitle(String title);
    List<Tag> findMostCostEffectiveTagByUser(long userId);
    BigDecimal getTagCost(long id);
    BigDecimal getTagCostByUser(long tagId, long userId);
    long getTagOrdersAmount(long id);
    long getTagOrdersAmount(long tagId, long userId);
    long countLastPage(List<Specification<Tag>> specifications, int size);
}
