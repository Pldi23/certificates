package com.epam.esm.service;

import com.epam.esm.converter.TagConverter;
import com.epam.esm.dto.MessageDTO;
import com.epam.esm.dto.TagDTO;
import com.epam.esm.entity.Tag;
import com.epam.esm.repository.AbstractTagRepository;
import com.epam.esm.specification.FindAllTagSpecification;
import com.epam.esm.specification.FindTagByIdSpecification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * gift certificates
 *
 * @author Dzmitry Platonov on 2019-09-25.
 * @version 0.0.1
 */
@Service(value = "TagService")
@Transactional
public class TagService {

    private ResourceBundleMessageSource messageSource;
    private AbstractTagRepository tagRepository;

    private TagConverter tagConverter;

    @Autowired
    public TagService(@Qualifier("TagRepository") AbstractTagRepository tagRepository, TagConverter tagConverter,
                      ResourceBundleMessageSource messageSource) {
        this.tagRepository = tagRepository;
        this.tagConverter = tagConverter;
        this.messageSource = messageSource;
    }

    public List<TagDTO> getTag(long id) {
        return tagRepository.query(new FindTagByIdSpecification(id)).stream()
                .map(tag -> tagConverter.convert(tag))
                .collect(Collectors.toList());
    }

    public List<TagDTO> findAll() {
        return tagRepository.query(new FindAllTagSpecification()).stream()
                .map(tag -> tagConverter.convert(tag))
                .collect(Collectors.toList());
    }

    public Optional<TagDTO> save(TagDTO tagDTO) {
        Tag tag = tagConverter.convert(tagDTO);
        Optional<Tag> optionalTag = tagRepository.add(tag);
        return optionalTag.map(value -> tagConverter.convert(value));
    }

    public void delete(long id) {
        tagRepository.remove(id);
    }

    public List<TagDTO> getTagsByCertificate(long id) {
        return tagRepository.getTagsByCertificate(id).stream()
                .map(tag -> tagConverter.convert(tag))
                .collect(Collectors.toList());
    }
}
