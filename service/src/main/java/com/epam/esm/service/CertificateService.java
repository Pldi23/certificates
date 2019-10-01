package com.epam.esm.service;

import com.epam.esm.converter.CertificateConverter;
import com.epam.esm.converter.CriteriaConverter;
import com.epam.esm.dto.*;
import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.repository.AbstractCertificateRepository;
import com.epam.esm.specification.FindAllCertificatesSpecification;
import com.epam.esm.specification.FindCertificateByIdSpecification;
import com.epam.esm.specification.FindCertificatesByCriteriaSpecification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * gift certificates
 *
 * @author Dzmitry Platonov on 2019-09-26.
 * @version 0.0.1
 */
@Component(value = "CertificateService")
@Transactional
public class CertificateService {

    private AbstractCertificateRepository certificateRepository;
    private ResourceBundleMessageSource messageSource;
    private CertificateConverter certificateConverter;
    private CriteriaConverter criteriaConverter;

    @Autowired
    public CertificateService(@Qualifier("CertificateRepository") AbstractCertificateRepository certificateRepository,
                              CertificateConverter certificateConverter,
                              CriteriaConverter criteriaConverter, ResourceBundleMessageSource messageSource) {
        this.certificateRepository = certificateRepository;
        this.certificateConverter = certificateConverter;
        this.criteriaConverter = criteriaConverter;
        this.messageSource = messageSource;
    }


    public List<GiftCertificateDTO> findAll() {
        return certificateRepository.query(new FindAllCertificatesSpecification()).stream()
                .map(giftCertificate -> certificateConverter.convert(giftCertificate))
                .collect(Collectors.toList());
    }


    public List<GiftCertificateDTO> findOneById(long id) {
        return certificateRepository.query(new FindCertificateByIdSpecification(id)).stream()
                .map(giftCertificate -> certificateConverter.convert(giftCertificate))
                .collect(Collectors.toList());
    }


    public MessageDTO save(GiftCertificateDTO giftCertificateDTO) {
        GiftCertificate giftCertificate = certificateConverter.convert(giftCertificateDTO);
        if (certificateRepository.query(new FindCertificateByIdSpecification(giftCertificate.getId())).isEmpty()) {
            certificateRepository.add(giftCertificate);
            return new MessageDTO(messageSource.getMessage("entity.save", null, null), 201);
        }
        return new MessageDTO(messageSource.getMessage("entity.exist", null, null), 400);

    }


    public MessageDTO update(GiftCertificateDTO giftCertificateDTO, long id) {
        GiftCertificate giftCertificate = certificateConverter.convert(giftCertificateDTO);
        giftCertificate.setId(id);
        if (!certificateRepository.query(new FindCertificateByIdSpecification(giftCertificate.getId())).isEmpty()) {
            certificateRepository.update(giftCertificate);
            return new MessageDTO(messageSource.getMessage("entity.update", null, null), 200);
        }
        return new MessageDTO(messageSource.getMessage("entity.no", null, null), 404);

    }


    public MessageDTO delete(long id) {
        certificateRepository.removeById(id);
        return new MessageDTO(messageSource.getMessage("entity.remove", null, null), 204);
    }


    public List<GiftCertificateDTO> findByCriteria(SearchCriteriaRequestDTO searchCriteriaDTO,
                                                   SortCriteriaRequestDTO sortCriteriaDTO,
                                                   LimitOffsetCriteriaRequestDTO limitOffsetCriteriaRequestDTO) {
        return certificateRepository.query(
                new FindCertificatesByCriteriaSpecification(
                        criteriaConverter.convertSearchCriteria(searchCriteriaDTO),
                        criteriaConverter.convertSortCriteria(sortCriteriaDTO),
                        criteriaConverter.convertLimitOffsetCriteria(limitOffsetCriteriaRequestDTO))).stream()
                .map(giftCertificate -> certificateConverter.convert(giftCertificate))
                .collect(Collectors.toList());
    }

    public List<GiftCertificateDTO> getByTag(long id) {
        return certificateRepository.getCertificatesByTagId(id).stream()
                .map(giftCertificate -> certificateConverter.convert(giftCertificate))
                .collect(Collectors.toList());
    }
}
