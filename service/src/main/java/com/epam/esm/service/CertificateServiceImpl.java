package com.epam.esm.service;

import com.epam.esm.converter.CertificateConverter;
import com.epam.esm.converter.CriteriaConverter;
import com.epam.esm.converter.TagConverter;
import com.epam.esm.dto.CertificatePatchDTO;
import com.epam.esm.dto.GiftCertificateDTO;
import com.epam.esm.dto.PageAndSortDTO;
import com.epam.esm.dto.SearchCriteriaRequestDTO;
import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.entity.Tag;
import com.epam.esm.exception.EntityAlreadyExistsException;
import com.epam.esm.repository.hibernate.EMCertificateRepository;
import com.epam.esm.repository.hibernate.EMTagRepository;
import com.epam.esm.validator.ExpirationDateValidator;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * implementation of {@link GiftCertificateDTO} service
 *
 * @author Dzmitry Platonov on 2019-09-26.
 * @version 0.0.1
 */
@Service
public class CertificateServiceImpl implements CertificateService {


    private EMCertificateRepository certificateRepository;
    private EMTagRepository tagRepository;
    private CertificateConverter certificateConverter;
    private CriteriaConverter criteriaConverter;
    private ExpirationDateValidator validator;
    private TagConverter tagConverter;

    public CertificateServiceImpl(EMCertificateRepository certificateRepository,
                                  EMTagRepository tagRepository,
                                  CertificateConverter certificateConverter,
                                  CriteriaConverter criteriaConverter,
                                  ExpirationDateValidator validator,
                                  TagConverter tagConverter) {
        this.certificateRepository = certificateRepository;
        this.tagRepository = tagRepository;
        this.certificateConverter = certificateConverter;
        this.criteriaConverter = criteriaConverter;
        this.validator = validator;
        this.tagConverter = tagConverter;
    }

    @Override
    public List<GiftCertificateDTO> findAll(PageAndSortDTO pageAndSortDTO) {
        return certificateRepository.findAll(pageAndSortDTO.getSortParameter(), pageAndSortDTO.getPage(), pageAndSortDTO.getSize()).stream()
                .map(giftCertificate -> certificateConverter.convert(giftCertificate))
                .collect(Collectors.toList());
    }

    @Override
    public GiftCertificateDTO findOne(long id) {
        return certificateConverter.convert(certificateRepository.findById(id).orElseThrow(() ->
                new EntityNotFoundException("certificate not found")));
    }

    @Transactional
    @Override
    public GiftCertificateDTO save(GiftCertificateDTO giftCertificateDTO) {
        Set<Tag> tags = giftCertificateDTO.getTags().stream()
                .map(tagDTO -> tagRepository.findByTitle(tagDTO.getTitle())
                        .orElseGet(() -> tagRepository.save(tagConverter.convert(tagDTO))))
                .collect(Collectors.toSet());
        GiftCertificate giftCertificate = certificateConverter.convert(giftCertificateDTO);
        giftCertificate.setCreationDate(LocalDate.now());
        giftCertificate.setModificationDate(null);
        validator.isValidDate(giftCertificate.getCreationDate(), giftCertificate.getExpirationDate());
        giftCertificate.setTags(tags);
        try {
            return certificateConverter.convert(certificateRepository.save(giftCertificate));
        } catch (DataIntegrityViolationException ex) {
            throw new EntityAlreadyExistsException("Certificate with name '" + giftCertificate.getName() + "' already exists");
        }
    }

    @Transactional
    @Override
    public GiftCertificateDTO update(GiftCertificateDTO giftCertificateDTO, long id) {
        Set<Tag> tags = giftCertificateDTO.getTags().stream()
                .map(tagDTO -> tagRepository.findByTitle(tagDTO.getTitle())
                        .orElseGet(() -> tagRepository.save(tagConverter.convert(tagDTO))))
                .collect(Collectors.toSet());
        GiftCertificate giftCertificate = certificateConverter.convert(giftCertificateDTO);
        giftCertificate.setId(id);
        giftCertificate.setModificationDate(LocalDate.now());
        GiftCertificate expectedCertificate = certificateRepository.findById(id).orElseThrow(() ->
                new EntityNotFoundException("certificate not found"));
        validator.isValidDate(expectedCertificate.getCreationDate(), giftCertificate.getExpirationDate());
        giftCertificate.setCreationDate(expectedCertificate.getCreationDate());
        giftCertificate.setTags(tags);
        return certificateConverter.convert(certificateRepository.save(giftCertificate));
    }

    @Transactional
    @Override
    public void delete(long id) {
        try {
            certificateRepository.deleteById(id);
        } catch (EmptyResultDataAccessException ex) {
            throw new EntityNotFoundException("certificate not found");
        }
    }


    @Override
    public List<GiftCertificateDTO> findByCriteria(SearchCriteriaRequestDTO searchCriteriaDTO, PageAndSortDTO pageAndSortDTO) {


//        return List.of();
        return certificateRepository.findByCriteria(
                        criteriaConverter.convertSearchCriteria(searchCriteriaDTO), pageAndSortDTO.getSortParameter(),
                pageAndSortDTO.getPage(), pageAndSortDTO.getSize()).stream()
                .map(giftCertificate -> certificateConverter.convert(giftCertificate))
                .collect(Collectors.toList());
    }

    @Override
    public List<GiftCertificateDTO> getByTag(long id, PageAndSortDTO pageAndSortDTO) {
        if (tagRepository.findById(id).isPresent()) {
            return certificateRepository.findByTagsId(id, pageAndSortDTO.getSortParameter(), pageAndSortDTO.getPage(),
                    pageAndSortDTO.getSize()).stream()
                    .map(giftCertificate -> certificateConverter.convert(giftCertificate))
                    .collect(Collectors.toList());
        } else {
            throw new EntityNotFoundException("tag not found");
        }
    }

    @Transactional
    @Override
    public GiftCertificateDTO patch(CertificatePatchDTO certificatePatchDTO, Long id) {
        GiftCertificate giftCertificate = certificateRepository.findById(id).orElseThrow(()
                -> new EntityNotFoundException("certificate not found"));
        if (certificatePatchDTO.getName() != null) {
            giftCertificate.setName(certificatePatchDTO.getName());
        }
        if (certificatePatchDTO.getDescription() != null) {
            giftCertificate.setDescription(certificatePatchDTO.getDescription());
        }
        if (certificatePatchDTO.getPrice() != null) {
            giftCertificate.setPrice(certificatePatchDTO.getPrice());
        }
        if (certificatePatchDTO.getExpirationDate() != null) {
            giftCertificate.setExpirationDate(certificatePatchDTO.getExpirationDate());
        }
        if (certificatePatchDTO.getTags() != null) {
            giftCertificate.setTags(certificatePatchDTO.getTags().stream()
                    .map(tagDTO -> tagConverter.convert(tagDTO))
                    .map(tag -> tagRepository.findByTitle(tag.getTitle())
                            .orElseGet(() -> tagRepository.save(tag)))
                    .collect(Collectors.toSet()));
        }
        return certificateConverter.convert(certificateRepository.save(giftCertificate));
    }

}
