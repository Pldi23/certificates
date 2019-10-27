package com.epam.esm.service;

import com.epam.esm.converter.CertificateConverter;
import com.epam.esm.converter.CriteriaConverter;
import com.epam.esm.converter.TagConverter;
import com.epam.esm.dto.CertificatePatchDTO;
import com.epam.esm.dto.GiftCertificateDTO;
import com.epam.esm.dto.PageAndSortDTO;
import com.epam.esm.dto.SearchCriteriaRequestDTO;
import com.epam.esm.dto.TagDTO;
import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.entity.Tag;
import com.epam.esm.entity.criteria.ParameterSearchType;
import com.epam.esm.entity.criteria.SearchCriteria;
import com.epam.esm.entity.criteria.TagCriteria;
import com.epam.esm.exception.EntityAlreadyExistsException;
import com.epam.esm.repository.AbstractCertificateRepository;
import com.epam.esm.repository.AbstractOrderRepository;
import com.epam.esm.repository.AbstractTagRepository;
import com.epam.esm.repository.page.PageSizeData;
import com.epam.esm.repository.predicate.CertificateHasCreationDateSpecification;
import com.epam.esm.repository.predicate.CertificateHasDescriptionSpecification;
import com.epam.esm.repository.predicate.CertificateHasExpirationDateSpecification;
import com.epam.esm.repository.predicate.CertificateHasIdSpecification;
import com.epam.esm.repository.predicate.CertificateHasModificationdateSpecification;
import com.epam.esm.repository.predicate.CertificateHasNameSpecification;
import com.epam.esm.repository.predicate.CertificateHasOrderIdSpecification;
import com.epam.esm.repository.predicate.CertificateHasPriceSpecification;
import com.epam.esm.repository.predicate.CertificateHasTagsIdSpecification;
import com.epam.esm.repository.predicate.CertificateHasTagsNameSpecification;
import com.epam.esm.repository.predicate.CertificateIsActiveSpecification;
import com.epam.esm.repository.predicate.Specification;
import com.epam.esm.repository.sort.CertificateSortData;
import com.epam.esm.util.Translator;
import com.epam.esm.validator.ExpirationDateValidator;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.time.LocalDate;
import java.util.ArrayList;
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

    private static final String CERTIFICATE_NOT_FOUND_MESSAGE = "entity.certificate.not.found";
    private static final String TAG_NOT_FOUND_MESSAGE = "entity.tag.not.found";


    private AbstractCertificateRepository certificateRepository;
    private AbstractTagRepository tagRepository;
    private AbstractOrderRepository orderRepository;
    private CertificateConverter certificateConverter;
    private CriteriaConverter criteriaConverter;
    private ExpirationDateValidator validator;
    private TagConverter tagConverter;

    public CertificateServiceImpl(AbstractCertificateRepository certificateRepository, AbstractTagRepository tagRepository,
                                  AbstractOrderRepository orderRepository, CertificateConverter certificateConverter,
                                  CriteriaConverter criteriaConverter, ExpirationDateValidator validator,
                                  TagConverter tagConverter) {
        this.certificateRepository = certificateRepository;
        this.tagRepository = tagRepository;
        this.orderRepository = orderRepository;
        this.certificateConverter = certificateConverter;
        this.criteriaConverter = criteriaConverter;
        this.validator = validator;
        this.tagConverter = tagConverter;
    }

    @Override
    public List<GiftCertificateDTO> findAll(PageAndSortDTO pageAndSortDTO) {
        return certificateRepository.findAllSpecified(List.of(new CertificateIsActiveSpecification()),
                pageAndSortDTO.getSortParameter() != null ? new CertificateSortData(pageAndSortDTO.getSortParameter()) : null,
                new PageSizeData(pageAndSortDTO.getPage(),
                pageAndSortDTO.getSize())).stream()
                .map(giftCertificate -> certificateConverter.convert(giftCertificate))
                .collect(Collectors.toList());
    }

    @Override
    public GiftCertificateDTO findOne(long id) {
        return certificateConverter.convert(certificateRepository.findById(id, true).orElseThrow(() ->
                new EntityNotFoundException(String.format(Translator.toLocale(CERTIFICATE_NOT_FOUND_MESSAGE), id))));
    }

    @Transactional
    @Override
    public GiftCertificateDTO save(GiftCertificateDTO giftCertificateDTO) {
        Set<Tag> tags = giftCertificateDTO.getTags().stream()
                .map(this::saveOrCreate)
                .collect(Collectors.toSet());
        GiftCertificate giftCertificate = certificateConverter.convert(giftCertificateDTO);
        giftCertificate.setCreationDate(LocalDate.now());
        giftCertificate.setModificationDate(null);
        validator.isValidDate(giftCertificate.getCreationDate(), giftCertificate.getExpirationDate());
        giftCertificate.setTags(tags);
        try {
            return certificateConverter.convert(certificateRepository.save(giftCertificate));
        } catch (DataIntegrityViolationException ex) {
            throw new EntityAlreadyExistsException(String.format(Translator.toLocale("exception.certificate.exist"), giftCertificateDTO.getName()));
        }
    }

    @Transactional
    @Override
    public GiftCertificateDTO update(GiftCertificateDTO giftCertificateDTO, long id) {
        Set<Tag> tags = giftCertificateDTO.getTags().stream()
                .map(this::saveOrCreate)
                .collect(Collectors.toSet());
        GiftCertificate giftCertificate = certificateConverter.convert(giftCertificateDTO);
        giftCertificate.setId(id);
        giftCertificate.setModificationDate(LocalDate.now());
        GiftCertificate expectedCertificate = certificateRepository.findById(id, true).orElseThrow(() ->
                new EntityNotFoundException(String.format(Translator.toLocale(CERTIFICATE_NOT_FOUND_MESSAGE), id)));
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
            throw new EntityNotFoundException(String.format(Translator.toLocale(CERTIFICATE_NOT_FOUND_MESSAGE), id));
        }
    }


    @Override
    public List<GiftCertificateDTO> findByCriteria(SearchCriteriaRequestDTO searchCriteriaDTO, PageAndSortDTO pageAndSortDTO) {
        List<Specification<GiftCertificate>> specifications = new ArrayList<>();
        SearchCriteria searchCriteria = criteriaConverter.convertSearchCriteria(searchCriteriaDTO);
        if (searchCriteria.getCreationDateCriteria() != null) {
            specifications.add(new CertificateHasCreationDateSpecification(searchCriteria.getCreationDateCriteria()));
        }
        if (searchCriteria.getExpirationDateCriteria() != null) {
            specifications.add(new CertificateHasExpirationDateSpecification(searchCriteria.getExpirationDateCriteria()));
        }
        if (searchCriteria.getModificationDateCriteria() != null) {
            specifications.add(new CertificateHasModificationdateSpecification(searchCriteria.getModificationDateCriteria()));
        }
        if (searchCriteria.getTagNameCriteria() != null) {
            specifications.add(new CertificateHasTagsNameSpecification(searchCriteria.getTagNameCriteria()));
        }
        if (searchCriteria.getTagCriteria() != null) {
            specifications.add(new CertificateHasTagsIdSpecification(searchCriteria.getTagCriteria()));
        }
        if (searchCriteria.getNameCriteria() != null) {
            specifications.add(new CertificateHasNameSpecification(searchCriteria.getNameCriteria()));
        }
        if (searchCriteria.getDescriptionCriteria() != null) {
            specifications.add(new CertificateHasDescriptionSpecification(searchCriteria.getDescriptionCriteria()));
        }
        if (searchCriteria.getIdCriteria() != null) {
            specifications.add(new CertificateHasIdSpecification(searchCriteria.getIdCriteria()));
        }
        if (searchCriteria.getPriceCriteria() != null) {
            specifications.add(new CertificateHasPriceSpecification(searchCriteria.getPriceCriteria()));
        }
        return certificateRepository.findAllSpecified(specifications,
                pageAndSortDTO.getSortParameter() != null ? new CertificateSortData(pageAndSortDTO.getSortParameter()) : null,
                new PageSizeData(pageAndSortDTO.getPage(), pageAndSortDTO.getSize())).stream()
                .map(giftCertificate -> certificateConverter.convert(giftCertificate))
                .collect(Collectors.toList());
    }

    @Override
    public List<GiftCertificateDTO> getByTag(long id, PageAndSortDTO pageAndSortDTO) {
        if (tagRepository.findById(id).isPresent()) {
            TagCriteria tagCriteria = new TagCriteria(ParameterSearchType.IN, List.of(id));
            return certificateRepository.findAllSpecified(List.of(new CertificateHasTagsIdSpecification(tagCriteria)),
                    pageAndSortDTO.getSortParameter() != null ? new CertificateSortData(pageAndSortDTO.getSortParameter()) : null,
                    new PageSizeData(pageAndSortDTO.getPage(), pageAndSortDTO.getSize())).stream()
                    .map(giftCertificate -> certificateConverter.convert(giftCertificate))
                    .collect(Collectors.toList());
        } else {
            throw new EntityNotFoundException(String.format(Translator.toLocale(TAG_NOT_FOUND_MESSAGE), id));
        }
    }

    @Override
    public List<GiftCertificateDTO> findByOrder(Long orderId, PageAndSortDTO pageAndSortDTO) {
        if (orderRepository.findById(orderId).isPresent()) {
            return certificateRepository.findAllSpecified(List.of(new CertificateHasOrderIdSpecification(orderId)),
                    pageAndSortDTO.getSortParameter() != null ? new CertificateSortData(pageAndSortDTO.getSortParameter()) : null,
                    new PageSizeData(pageAndSortDTO.getPage(), pageAndSortDTO.getSize())).stream()
                    .map(giftCertificate -> certificateConverter.convert(giftCertificate))
                    .collect(Collectors.toList());
        } else {
            throw new EntityNotFoundException(String.format(Translator.toLocale("entity.order.not.found"), orderId));
        }
    }

    @Transactional
    @Override
    public GiftCertificateDTO patch(CertificatePatchDTO certificatePatchDTO, Long id) {
        GiftCertificate giftCertificate = certificateRepository.findById(id, true).orElseThrow(()
                -> new EntityNotFoundException(String.format(Translator.toLocale(CERTIFICATE_NOT_FOUND_MESSAGE), id)));
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

    private Tag saveOrCreate(TagDTO tagDTO) {
        if (tagDTO.getId() == null && tagDTO.getTitle() != null) {
            return tagRepository.findByTitle(tagDTO.getTitle())
                    .orElseGet(() -> tagRepository.save(tagConverter.convert(tagDTO)));
        } else if (tagDTO.getId() != null && tagDTO.getTitle() == null) {
            return tagRepository.findById(tagDTO.getId())
                    .orElseThrow(() -> new EntityNotFoundException(String.format(Translator.toLocale(TAG_NOT_FOUND_MESSAGE), tagDTO.getId())));
        } else {
            Tag tag = tagRepository.findById(tagDTO.getId())
                    .orElseThrow(() -> new EntityNotFoundException(String.format(Translator.toLocale(TAG_NOT_FOUND_MESSAGE), tagDTO.getId())));
            if (tag.getTitle().equals(tagDTO.getTitle())) {
                return tag;
            } else {
                throw new EntityNotFoundException(String.format(Translator.toLocale("entity.tag.by.title.id.not.found"), tagDTO.getId(), tagDTO.getTitle()));
            }
        }
    }

}
