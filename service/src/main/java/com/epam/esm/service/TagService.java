package com.epam.esm.service;

import com.epam.esm.dto.PageAndSortDTO;
import com.epam.esm.dto.PageableList;
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

    PageableList<TagDTO> findTagsByOrder(long orderId, PageAndSortDTO pageAndSortDTO);

    PageableList<TagDTO> findTagsByUser(long userId, PageAndSortDTO pageAndSortDTO);

    PageableList<TagDTO> findPopular(PageAndSortDTO pageAndSortDTO);

    PageableList<TagDTO> getTagsByCertificate(long id, PageAndSortDTO pageAndSortDTO);

    PageableList<TagDTO> findAllPaginated(PageAndSortDTO pageAndSortDTO);

    List<TagDTO> findMostCostEffectiveTag(Long userId);

    List<TagDetailsDTO> findMostCostEffectiveTagWithStats(Long userId);

    BigDecimal calculateTagCost(long id);

    PageableList<TagDetailsDTO> findTagsWithDetails(PageAndSortDTO pageAndSortDTO);

    TagDetailsDTO findTagDetails(long id);

    List<TagDTO> deleteUnlinkedTags();
}
