package com.epam.esm.service;

import com.epam.esm.converter.TagConverter;
import com.epam.esm.dto.TagDTO;
import com.epam.esm.entity.Tag;
import com.epam.esm.repository.Repository;
import com.epam.esm.specification.FindAllTagSpecification;
import com.epam.esm.specification.FindByTitlePartSpecification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * gift certificates
 *
 * @author Dzmitry Platonov on 2019-09-25.
 * @version 0.0.1
 */
@Service
@Transactional
public class TagService {


    @Qualifier(value = "TagRepository")
    private Repository<Tag> tagRepository;

    private TagConverter tagConverter;

    @Autowired
    public TagService(Repository<Tag> tagRepository, TagConverter tagConverter) {
        this.tagRepository = tagRepository;
        this.tagConverter = tagConverter;
    }

    public List<TagDTO> getTag(String title) {
        return tagRepository.query(new FindByTitlePartSpecification(title)).stream()
                .map(tag -> tagConverter.convert(tag))
                .collect(Collectors.toList());
    }

    public List<TagDTO> getAllTags() {
        return tagRepository.query(new FindAllTagSpecification()).stream()
                .map(tag -> tagConverter.convert(tag))
                .collect(Collectors.toList());
    }
}
