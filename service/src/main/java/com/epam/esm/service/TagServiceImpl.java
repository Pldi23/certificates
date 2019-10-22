package com.epam.esm.service;

import com.epam.esm.converter.TagConverter;
import com.epam.esm.dto.PageAndSortDTO;
import com.epam.esm.dto.TagDTO;
import com.epam.esm.entity.Tag;
import com.epam.esm.exception.EntityAlreadyExistsException;
import com.epam.esm.repository.AbstractCertificateRepository;
import com.epam.esm.repository.AbstractTagRepository;
import com.epam.esm.repository.EMCertificateRepository;
import com.epam.esm.repository.EMTagRepository;
import com.epam.esm.util.Translator;
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

    private AbstractTagRepository tagRepository;
    private AbstractCertificateRepository certificateRepository;

    private TagConverter tagConverter;

    public TagServiceImpl(AbstractTagRepository tagRepository, AbstractCertificateRepository certificateRepository, TagConverter tagConverter) {
        this.tagRepository = tagRepository;
        this.certificateRepository = certificateRepository;
        this.tagConverter = tagConverter;
    }

    @Override
    public TagDTO findOne(long id) {
        return tagConverter.convert(tagRepository.findById(id).orElseThrow(() -> new EntityNotFoundException(String.format(Translator.toLocale("entity.tag.not.found"), id))));
    }

    @Override
    public List<TagDTO> findAll(PageAndSortDTO pageAndSortDTO) {
        return tagRepository.findAll(pageAndSortDTO.getSortParameter(), pageAndSortDTO.getPage(), pageAndSortDTO.getSize()).stream()
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
            throw new EntityAlreadyExistsException(String.format(Translator.toLocale("exception.tag.exist"), tagDTO.getTitle()));
        }
        return tagConverter.convert(saved);
    }

    @Override
    public void delete(long id) {
        try {
            tagRepository.deleteById(id);
        } catch (EmptyResultDataAccessException ex) {
            throw new EntityNotFoundException(String.format(Translator.toLocale("entity.tag.not.found"), id));
        }
    }

    @Override
    public List<TagDTO> getTagsByCertificate(long id, PageAndSortDTO pageAndSortDTO) {
        if (certificateRepository.findById(id, true).isPresent()) {
            return tagRepository.findTagsByCertificate(id, pageAndSortDTO.getSortParameter(), pageAndSortDTO.getPage(),
                    pageAndSortDTO.getSize()).stream()
                    .map(tag -> tagConverter.convert(tag))
                    .collect(Collectors.toList());
        } else {
            throw new EntityNotFoundException(String.format(Translator.toLocale("entity.certificate.not.found"), id));
        }
    }

    @Override
    public List<TagDTO> findPaginated(PageAndSortDTO pageAndSortDTO) {
        return tagRepository.findPaginated(pageAndSortDTO.getSortParameter(), pageAndSortDTO.getPage(), pageAndSortDTO.getSize()).stream()
                .map(tag -> tagConverter.convert(tag))
                .collect(Collectors.toList());
    }

    @Override
    public List<TagDTO> findMostCostEffectiveTag(Long userId) {
        List<Tag> tags = tagRepository.findMostCostEffectiveTagByUser(userId);
        return tags.stream()
                .map(tag -> tagConverter.convert(tag))
                .collect(Collectors.toList());
    }

    @Override
    public long count() {
        return tagRepository.count();
    }

    @Override
    public List<TagDTO> findTagsByOrder(long orderId, PageAndSortDTO pageAndSortDTO) {
        return tagRepository.findTagsByOrder(orderId, pageAndSortDTO.getSortParameter(), pageAndSortDTO.getPage(),
                pageAndSortDTO.getSize()).stream()
                .map(tag -> tagConverter.convert(tag))
                .collect(Collectors.toList());
    }

    @Override
    public List<TagDTO> findTagsByUser(long userId, PageAndSortDTO pageAndSortDTO) {
        return tagRepository.findTagsByUserWithCriteria(userId, pageAndSortDTO.getSortParameter(), pageAndSortDTO.getPage(), pageAndSortDTO.getSize()).stream()
                .map(tag -> tagConverter.convert(tag))
                .collect(Collectors.toList());
    }

    @Override
    public List<TagDTO> findPopular(PageAndSortDTO pageAndSortDTO) {
        return tagRepository.findPopulars(pageAndSortDTO.getPage(), pageAndSortDTO.getSize()).stream()
                .map(tag -> tagConverter.convert(tag))
                .collect(Collectors.toList());
    }
}
