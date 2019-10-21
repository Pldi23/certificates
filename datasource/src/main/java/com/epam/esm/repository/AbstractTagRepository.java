package com.epam.esm.repository;

import com.epam.esm.entity.Tag;

import java.util.List;
import java.util.Optional;


public interface AbstractTagRepository extends FindAllRepository<Tag>, FindOneRepository<Tag>, SaveRepository<Tag>,
        RemoveRepository, QueryRepository<Tag>  {

    List<Tag> findTagsByCertificate(long certificateId, String sortParam, int page, int size);
    Optional<Tag> findByTitle(String title);
    List<Tag> findPaginated(String sortParam, int page, int size);
    List<Tag> findTagsByOrder(long orderId, String sortParam, int page, int size);
    List<Tag> findTagsByUserWithCriteria(long userId, String sortParam, int page, int size);
    List<Tag> findPopulars(int page, int size);
    List<Tag> findMostCostEffectiveTagByUser(long userId);
    long count();
}
