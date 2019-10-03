package com.epam.esm.service;

import com.epam.esm.converter.CertificateConverter;
import com.epam.esm.converter.CriteriaConverter;
import com.epam.esm.dto.*;
import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.repository.AbstractCertificateRepository;
import com.epam.esm.specification.FindCertificatesByCriteriaSpecification;
import com.epam.esm.specification.FindCertificatesByTagSpecification;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * implementation of {@link GiftCertificateDTO} service
 *
 * @author Dzmitry Platonov on 2019-09-26.
 * @version 0.0.1
 */
@Component
public class CertificateServiceImpl implements CertificateService {


    private AbstractCertificateRepository certificateRepository;
    private CertificateConverter certificateConverter;
    private CriteriaConverter criteriaConverter;

    public CertificateServiceImpl(AbstractCertificateRepository certificateRepository,
                                  CertificateConverter certificateConverter,
                                  CriteriaConverter criteriaConverter) {
        this.certificateRepository = certificateRepository;
        this.certificateConverter = certificateConverter;
        this.criteriaConverter = criteriaConverter;
    }

    @Override
    public List<GiftCertificateDTO> findAll() {
        return certificateRepository.findAll().stream()
                .map(giftCertificate -> certificateConverter.convert(giftCertificate))
                .collect(Collectors.toList());
    }

    @Override
    public Optional<GiftCertificateDTO> findOne(long id) {
        Optional<GiftCertificate> optionalGiftCertificate = certificateRepository.findOne(id);
        return optionalGiftCertificate.map(giftCertificate -> certificateConverter.convert(giftCertificate));
    }

    @Override
    public Optional<GiftCertificateDTO> save(GiftCertificateDTO giftCertificateDTO) {
        GiftCertificate giftCertificate = certificateConverter.convert(giftCertificateDTO);
        Optional<GiftCertificate> optionalGiftCertificate = certificateRepository.save(giftCertificate);
        return optionalGiftCertificate.map(certificate -> certificateConverter.convert(certificate));
    }

    @Override
    public boolean update(GiftCertificateDTO giftCertificateDTO, long id) {
        GiftCertificate giftCertificate = certificateConverter.convert(giftCertificateDTO);
        giftCertificate.setId(id);
        return certificateRepository.update(giftCertificate);

    }

    @Override
    public void delete(long id) {
        certificateRepository.remove(id);
    }

    @Override
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

    @Override
    public List<GiftCertificateDTO> getByTag(long id) {
        return certificateRepository.query(new FindCertificatesByTagSpecification(id)).stream()
                .map(giftCertificate -> certificateConverter.convert(giftCertificate))
                .collect(Collectors.toList());
    }
}
