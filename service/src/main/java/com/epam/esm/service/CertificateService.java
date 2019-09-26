package com.epam.esm.service;

import com.epam.esm.converter.CertificateConverter;
import com.epam.esm.converter.CriteriaConverter;
import com.epam.esm.dto.GiftCertificateDTO;
import com.epam.esm.dto.MessageDTO;
import com.epam.esm.dto.SearchCriteriaDTO;
import com.epam.esm.dto.SortCriteriaDTO;
import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.repository.Repository;
import com.epam.esm.specification.FindAllCertificatesSpecification;
import com.epam.esm.specification.FindCertificateByIdSpecification;
import com.epam.esm.specification.FindCertificatesByCriteriaSpecification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
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

    @Qualifier(value = "CertificateRepository")
    private Repository<GiftCertificate> certificateRepository;

    private CertificateConverter certificateConverter;
    private CriteriaConverter criteriaConverter;

    @Autowired
    public CertificateService(Repository<GiftCertificate> certificateRepository,
                              CertificateConverter certificateConverter,
                              CriteriaConverter criteriaConverter) {
        this.certificateRepository = certificateRepository;
        this.certificateConverter = certificateConverter;
        this.criteriaConverter = criteriaConverter;
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
        String message;
        if (certificateRepository.query(new FindCertificateByIdSpecification(giftCertificate.getId())).isEmpty()) {
            certificateRepository.add(giftCertificate);
            message = "successfully added";
        } else {
            message = "already exists";
        }
        return new MessageDTO(message);
    }

    public MessageDTO update(GiftCertificateDTO giftCertificateDTO) {
        GiftCertificate giftCertificate = certificateConverter.convert(giftCertificateDTO);
        String message;
        if (!certificateRepository.query(new FindCertificateByIdSpecification(giftCertificate.getId())).isEmpty()) {
            certificateRepository.update(giftCertificate);
            message = "successfully updated";
        } else {
            message = "gift certificate id: " + giftCertificateDTO.getId() + " no found";
        }
        return new MessageDTO(message);
    }

    public MessageDTO delete(long id) {
        List<GiftCertificate> certificates = certificateRepository.query(new FindCertificateByIdSpecification(id));
        String message;
        if (!certificates.isEmpty()) {
            certificateRepository.remove(certificates.get(0));
            message = "successfully removed";
        } else {
            message = "certificate with id: " + id + " not found";
        }
        return new MessageDTO(message);
    }

    public List<GiftCertificateDTO> findByCriteria(SearchCriteriaDTO searchCriteriaDTO, SortCriteriaDTO sortCriteriaDTO) {
        return certificateRepository.query(
                new FindCertificatesByCriteriaSpecification(
                        criteriaConverter.convertSearchCriteria(searchCriteriaDTO),
                        criteriaConverter.convertSortCriteria(sortCriteriaDTO))).stream()
                .map(giftCertificate -> certificateConverter.convert(giftCertificate))
                .collect(Collectors.toList());
    }
}
