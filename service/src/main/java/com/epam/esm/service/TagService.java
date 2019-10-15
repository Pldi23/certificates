package com.epam.esm.service;

import com.epam.esm.dto.PageAndSortDTO;
import com.epam.esm.dto.TagDTO;

import java.util.List;


/**
 * to encapsulate service operations around {@link TagDTO}
 *
 * @author Dzmitry Platonov on 2019-10-01.
 * @version 0.0.1
 */
public interface TagService extends FindOneService<TagDTO>, FindAllService<TagDTO>, SaveService<TagDTO>, DeleteService,
        FindTagsByCertificateService, FindPaginated<TagDTO> {

    List<TagDTO> findTagsByOrder(long orderId);
    List<TagDTO> findTagsByUser(long userId, PageAndSortDTO pageAndSortDTO);
    List<TagDTO> findPopular(PageAndSortDTO pageAndSortDTO);
}
