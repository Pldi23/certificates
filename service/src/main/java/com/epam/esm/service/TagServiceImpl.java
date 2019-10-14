package com.epam.esm.service;

import com.epam.esm.converter.TagConverter;
import com.epam.esm.dto.TagDTO;
import com.epam.esm.entity.Tag;
import com.epam.esm.exception.EntityAlreadyExistsException;
import com.epam.esm.repository.hibernate.EMCertificateRepository;
import com.epam.esm.repository.hibernate.EMTagRepository;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.stream.Collectors;

/**
 * implementation of {@link TagDTO} service
 *
 * @author Dzmitry Platonov on 2019-09-25.
 * @version 0.0.1
 */
@Transactional
@Service
public class TagServiceImpl implements TagService {

    private EMTagRepository tagRepository;
    private EMCertificateRepository certificateRepository;

    private TagConverter tagConverter;

    public TagServiceImpl(EMTagRepository tagRepository, EMCertificateRepository certificateRepository, TagConverter tagConverter) {
        this.tagRepository = tagRepository;
        this.certificateRepository = certificateRepository;
        this.tagConverter = tagConverter;
    }

    @Override
    public TagDTO findOne(long id) {
        return tagConverter.convert(tagRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("tag not found")));
    }

    @Override
    public List<TagDTO> findAll() {
        return tagRepository.findAll().stream()
                .map(tag -> tagConverter.convert(tag))
                .collect(Collectors.toList());
    }

    @Override
    public TagDTO save(TagDTO tagDTO) {
        Tag tag = tagConverter.convert(tagDTO);
        Tag saved;
        try {
            saved = tagRepository.save(tag);
        } catch (DataIntegrityViolationException ex) {
            throw new EntityAlreadyExistsException("Tag with name '" + tagDTO.getTitle() + "' already exists");
        }
        return tagConverter.convert(saved);
//        return optionalTag.map(value -> tagConverter.convert(value));
    }

    @Override
    public void delete(long id) {
        try {
            tagRepository.deleteById(id);
        } catch (EmptyResultDataAccessException ex) {
            throw new EntityNotFoundException("tag not found");
        }
    }

    @Override
    public List<TagDTO> getTagsByCertificate(long id) {
        if (certificateRepository.findById(id).isPresent()) {
            return tagRepository.findTagsByCertificate(id).stream()
                    .map(tag -> tagConverter.convert(tag))
                    .collect(Collectors.toList());
        } else {
            throw new EntityNotFoundException("certificate not found");
        }
    }

    @Override
    public List<TagDTO> findPaginated(String sort, int page, int size) {
        return tagRepository.findPaginated(sort, page, size).stream()
                .map(tag -> tagConverter.convert(tag))
                .collect(Collectors.toList());
    }
}
