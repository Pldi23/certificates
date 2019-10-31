package com.epam.esm.service;

import com.epam.esm.dto.PageAndSortDTO;
import com.epam.esm.dto.TagDTO;
import com.epam.esm.dto.TagDetailsDTO;

import java.math.BigDecimal;
import java.util.List;


/**
 * to encapsulate service operations around {@link TagDTO}
 *
 * @author Dzmitry Platonov on 2019-10-01.
 * @version 0.0.1
 */
public interface TagService extends FindOneService<TagDTO>, FindAllService<TagDTO>, SaveService<TagDTO>, DeleteService{

    List<TagDTO> findTagsByOrder(long orderId, PageAndSortDTO pageAndSortDTO);

    List<TagDTO> findTagsByUser(long userId, PageAndSortDTO pageAndSortDTO);

    List<TagDTO> findPopular(PageAndSortDTO pageAndSortDTO);

    List<TagDTO> getTagsByCertificate(long id, PageAndSortDTO pageAndSortDTO);

    List<TagDTO> findAllPaginated(PageAndSortDTO pageAndSortDTO);

    List<TagDTO> findMostCostEffectiveTag(Long userId);

    List<TagDetailsDTO> findMostCostEffectiveTagWithStats(Long userId);

    BigDecimal calculateTagCost(long id);

    List<TagDetailsDTO> findTagsWithDetails(PageAndSortDTO pageAndSortDTO);

    TagDetailsDTO findTagDetails(long id);
}
