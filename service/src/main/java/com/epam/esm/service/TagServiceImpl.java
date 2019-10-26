package com.epam.esm.service;

import com.epam.esm.converter.TagConverter;
import com.epam.esm.dto.PageAndSortDTO;
import com.epam.esm.dto.TagDTO;
import com.epam.esm.entity.Tag;
import com.epam.esm.exception.EntityAlreadyExistsException;
import com.epam.esm.repository.AbstractCertificateRepository;
import com.epam.esm.repository.AbstractOrderRepository;
import com.epam.esm.repository.AbstractTagRepository;
import com.epam.esm.repository.AbstractUserRepository;
import com.epam.esm.repository.constant.JpaConstant;
import com.epam.esm.repository.page.PageSizeData;
import com.epam.esm.repository.predicate.TagHasCertificateByIdSpecification;
import com.epam.esm.repository.predicate.TagHasOrderSpecification;
import com.epam.esm.repository.predicate.TagHasUserSpecification;
import com.epam.esm.repository.sort.TagSortData;
import com.epam.esm.util.Translator;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.math.BigDecimal;
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
    private AbstractOrderRepository orderRepository;
    private AbstractUserRepository userRepository;
    private TagConverter tagConverter;

    public TagServiceImpl(AbstractTagRepository tagRepository, AbstractCertificateRepository certificateRepository,
                          AbstractOrderRepository orderRepository, AbstractUserRepository userRepository,
                          TagConverter tagConverter) {
        this.tagRepository = tagRepository;
        this.certificateRepository = certificateRepository;
        this.orderRepository = orderRepository;
        this.userRepository = userRepository;
        this.tagConverter = tagConverter;
    }

    @Override
    public TagDTO findOne(long id) {
        return tagConverter.convert(tagRepository.findById(id).orElseThrow(() ->
                new EntityNotFoundException(String.format(Translator.toLocale("entity.tag.not.found"), id))));
    }

    @Override
    public List<TagDTO> findAll(PageAndSortDTO pageAndSortDTO) {
        return tagRepository.findAllSpecified(null,null,
                new PageSizeData(pageAndSortDTO.getPage(), pageAndSortDTO.getSize())).stream()
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
            return tagRepository.findAllSpecified(List.of(new TagHasCertificateByIdSpecification(id)),
                    new TagSortData(pageAndSortDTO.getSortParameter()),
                    new PageSizeData(pageAndSortDTO.getPage(), pageAndSortDTO.getSize())).stream()
                    .map(tag -> tagConverter.convert(tag))
                    .collect(Collectors.toList());
        } else {
            throw new EntityNotFoundException(String.format(Translator.toLocale("entity.certificate.not.found"), id));
        }
    }



    @Override
    public List<TagDTO> findAllPaginated(PageAndSortDTO pageAndSortDTO) {
        return tagRepository.findAllSpecified(null,
                pageAndSortDTO.getSortParameter() != null ? new TagSortData(pageAndSortDTO.getSortParameter()) : null,
                new PageSizeData(pageAndSortDTO.getPage(), pageAndSortDTO.getSize())).stream()
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
    public BigDecimal calculateTagCost(long id) {
        return tagRepository.getTagCost(id);
    }

    @Override
    public List<TagDTO> findTagsByOrder(long orderId, PageAndSortDTO pageAndSortDTO) {
        if (orderRepository.findById(orderId).isPresent()) {
            return tagRepository.findAllSpecified(List.of(new TagHasOrderSpecification(orderId)),
                    pageAndSortDTO.getSortParameter() != null ? new TagSortData(pageAndSortDTO.getSortParameter()) : null,
                    new PageSizeData(pageAndSortDTO.getPage(), pageAndSortDTO.getSize())).stream()
                    .map(tag -> tagConverter.convert(tag))
                    .collect(Collectors.toList());
        } else {
            throw new EntityNotFoundException(String.format(Translator.toLocale("entity.order.not.found"), orderId));
        }
    }

    @Override
    public List<TagDTO> findTagsByUser(long userId, PageAndSortDTO pageAndSortDTO) {
        if (userRepository.findById(userId).isPresent()) {
            return tagRepository.findAllSpecified(List.of(new TagHasUserSpecification(userId)),
                    pageAndSortDTO.getSortParameter() != null ? new TagSortData(pageAndSortDTO.getSortParameter()) : null,
                    new PageSizeData(pageAndSortDTO.getPage(), pageAndSortDTO.getSize())).stream()
                    .map(tag -> tagConverter.convert(tag))
                    .collect(Collectors.toList());
        } else {
            throw new EntityNotFoundException(String.format(Translator.toLocale("entity.user.id.not.found"), userId));
        }
    }

    @Override
    public List<TagDTO> findPopular(PageAndSortDTO pageAndSortDTO) {
        return tagRepository.findAllSpecified(null, new TagSortData(JpaConstant.COST),
                new PageSizeData(pageAndSortDTO.getPage(), pageAndSortDTO.getSize())).stream()
                .map(tag -> tagConverter.convert(tag))
                .collect(Collectors.toList());
    }
}
