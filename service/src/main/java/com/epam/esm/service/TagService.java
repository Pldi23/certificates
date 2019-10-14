package com.epam.esm.service;

import com.epam.esm.dto.TagDTO;


/**
 * to encapsulate service operations around {@link TagDTO}
 *
 * @author Dzmitry Platonov on 2019-10-01.
 * @version 0.0.1
 */
public interface TagService extends FindOneService<TagDTO>, FindAllService<TagDTO>, SaveService<TagDTO>, DeleteService,
        FindTagsByCertificateService, FindPaginated<TagDTO> {


}
