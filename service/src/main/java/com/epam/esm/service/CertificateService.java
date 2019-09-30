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
public class CertificateServiceImpl implements CertificateService {

    private static final String SAVE_SUCCESS = "successfully added";
    private static final String UPDATE_SUCCESS = "successfully updated";
    private static final String REMOVE_SUCCESS = "successfully removed";
    private static final String NOT_FOUND = "gift certificate no found";
    private static final String EXISTS = "already exists";

    @Qualifier(value = "CertificateRepository")
    private AbstractCertificateRepository certificateRepository;

    private CertificateConverter certificateConverter;
    private CriteriaConverter criteriaConverter;

    @Autowired
    public CertificateServiceImpl(AbstractCertificateRepository certificateRepository,
                                  CertificateConverter certificateConverter,
                                  CriteriaConverter criteriaConverter) {
        this.certificateRepository = certificateRepository;
        this.certificateConverter = certificateConverter;
        this.criteriaConverter = criteriaConverter;
    }

    @Override
    public List<GiftCertificateDTO> findAll() {
        return certificateRepository.query(new FindAllCertificatesSpecification()).stream()
                .map(giftCertificate -> certificateConverter.convert(giftCertificate))
                .collect(Collectors.toList());
    }

    @Override
    public List<GiftCertificateDTO> findOneById(long id) {
        return certificateRepository.query(new FindCertificateByIdSpecification(id)).stream()
                .map(giftCertificate -> certificateConverter.convert(giftCertificate))
                .collect(Collectors.toList());
    }

    @Override
    public MessageDTO save(GiftCertificateDTO giftCertificateDTO) {
        GiftCertificate giftCertificate = certificateConverter.convert(giftCertificateDTO);
        MessageDTO messageDTO;
        if (certificateRepository.query(new FindCertificateByIdSpecification(giftCertificate.getId())).isEmpty()) {
            certificateRepository.add(giftCertificate);
            messageDTO = new MessageDTO(SAVE_SUCCESS, 201);
        } else {
            messageDTO = new MessageDTO(EXISTS, 400);
        }
        return messageDTO;
    }

    @Override
    public MessageDTO update(GiftCertificateDTO giftCertificateDTO, long id) {
        GiftCertificate giftCertificate = certificateConverter.convert(giftCertificateDTO);
        giftCertificate.setId(id);
        MessageDTO messageDTO;
        if (!certificateRepository.query(new FindCertificateByIdSpecification(giftCertificate.getId())).isEmpty()) {
            certificateRepository.update(giftCertificate);
            messageDTO = new MessageDTO(UPDATE_SUCCESS, 200);
        } else {
            messageDTO = new MessageDTO(NOT_FOUND, 400);
        }
        return messageDTO;
    }

    @Override
    public MessageDTO delete(long id) {
        List<GiftCertificate> certificates = certificateRepository.query(new FindCertificateByIdSpecification(id));
        MessageDTO messageDTO;
        if (!certificates.isEmpty()) {
            certificateRepository.remove(certificates.get(0));
            messageDTO = new MessageDTO(REMOVE_SUCCESS, 200);
        } else {
            messageDTO = new MessageDTO(NOT_FOUND, 400);
        }
        return messageDTO;
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

    public List<GiftCertificateDTO> getByFunction(String desc, String name) {
        return certificateRepository.getCertificatesByNameOrDescription(desc, name).stream()
                .map(giftCertificate -> certificateConverter.convert(giftCertificate))
                .collect(Collectors.toList());
    }
}
