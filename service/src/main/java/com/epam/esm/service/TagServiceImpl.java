package com.epam.esm.service;

import com.epam.esm.converter.TagConverter;
import com.epam.esm.dto.TagDTO;
import com.epam.esm.entity.Tag;
import com.epam.esm.repository.Repository;
import com.epam.esm.specification.FindAllTagSpecification;
import com.epam.esm.specification.FindTagsByCertificateSpecification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * gift certificates
 *
 * @author Dzmitry Platonov on 2019-09-25.
 * @version 0.0.1
 */
@Service
public class TagServiceImpl implements TagService {

    private Repository<Tag> tagRepository;

    private TagConverter tagConverter;

    @Autowired
    public TagServiceImpl(Repository<Tag> tagRepository, TagConverter tagConverter) {
        this.tagRepository = tagRepository;
        this.tagConverter = tagConverter;
    }

    @Override
    public Optional<TagDTO> getTag(long id) {
        Optional<Tag> optionalTag = tagRepository.findOne(id);
        return optionalTag.map(tag -> tagConverter.convert(tag));
    }

    @Override
    public List<TagDTO> findAll() {
        return tagRepository.query(new FindAllTagSpecification()).stream()
                .map(tag -> tagConverter.convert(tag))
                .collect(Collectors.toList());
    }

    @Override
    public Optional<TagDTO> save(TagDTO tagDTO) {
        Tag tag = tagConverter.convert(tagDTO);
        Optional<Tag> optionalTag = tagRepository.add(tag);
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
