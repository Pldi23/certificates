package com.epam.esm.converter;

import com.epam.esm.dto.TagDTO;
import com.epam.esm.entity.Tag;
import org.springframework.stereotype.Component;

/**
 * giftcertificates
 *
 * @author Dzmitry Platonov on 2019-09-25.
 * @version 0.0.1
 */
@Component
public class TagConverter {

    public TagDTO convert(Tag tag) {
        return new TagDTO(tag.getTitle());
    }

    public Tag convert(TagDTO tagDTO) {
        return new Tag(tagDTO.getTitle());
    }
}
