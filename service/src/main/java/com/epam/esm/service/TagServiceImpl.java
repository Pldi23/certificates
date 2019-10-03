package com.epam.esm.service;

import com.epam.esm.converter.TagConverter;
import com.epam.esm.dto.TagDTO;
import com.epam.esm.entity.Tag;
import com.epam.esm.repository.AbstractTagRepository;
import com.epam.esm.specification.FindTagsByCertificateSpecification;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * implementation of {@link TagDTO} service
 *
 * @author Dzmitry Platonov on 2019-09-25.
 * @version 0.0.1
 */
@Service
public class TagServiceImpl implements TagService {

    private AbstractTagRepository tagRepository;

    private TagConverter tagConverter;

    public TagServiceImpl(AbstractTagRepository tagRepository, TagConverter tagConverter) {
        this.tagRepository = tagRepository;
        this.tagConverter = tagConverter;
    }

    @Override
    public Optional<TagDTO> findOne(long id) {
        Optional<Tag> optionalTag = tagRepository.findOne(id);
        return optionalTag.map(tag -> tagConverter.convert(tag));
    }

    @Override
    public List<TagDTO> findAll() {
        return tagRepository.findAll().stream()
                .map(tag -> tagConverter.convert(tag))
                .collect(Collectors.toList());
    }

    @Override
    public Optional<TagDTO> save(TagDTO tagDTO) {
        Tag tag = tagConverter.convert(tagDTO);
        Optional<Tag> optionalTag = tagRepository.save(tag);
        return optionalTag.map(value -> tagConverter.convert(value));
    }

    @Override
    public void delete(long id) {
        tagRepository.remove(id);
    }

    @Override
    public List<TagDTO> getTagsByCertificate(long id) {
        return tagRepository.query(new FindTagsByCertificateSpecification(id)).stream()
                .map(tag -> tagConverter.convert(tag))
                .collect(Collectors.toList());
    }
}
