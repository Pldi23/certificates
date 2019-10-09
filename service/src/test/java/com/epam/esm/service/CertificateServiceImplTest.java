package com.epam.esm.service;

import com.epam.esm.converter.CertificateConverter;
import com.epam.esm.converter.CriteriaConverter;
import com.epam.esm.converter.CriteriaCreatorHelper;
import com.epam.esm.dto.*;
import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.repository.AbstractCertificateRepository;
import com.epam.esm.validator.ExpirationDateValidator;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.context.support.ResourceBundleMessageSource;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;

/**
 * gift certificates
 *
 * @author Dzmitry Platonov on 2019-09-29.
 * @version 0.0.1
 */
@RunWith(MockitoJUnitRunner.class)
public class CertificateServiceImplTest {

    @InjectMocks
    private CertificateServiceImpl service;

    @Mock
    private AbstractCertificateRepository repository;

    private CertificateConverter converter;
    private GiftCertificate giftCertificate;
    private GiftCertificateDTO giftCertificateDTO;


    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        converter = new CertificateConverter();
        CriteriaCreatorHelper helper = new CriteriaCreatorHelper();
        CriteriaConverter criteriaConverter = new CriteriaConverter(helper);
        ResourceBundleMessageSource messageSource = new ResourceBundleMessageSource();
        ExpirationDateValidator validator = new ExpirationDateValidator(messageSource);
        service = new CertificateServiceImpl(repository, converter, criteriaConverter, validator);

        giftCertificate = new GiftCertificate.Builder()
                .withId(4L)
                .withName("flowers")
                .withDescription("one hundred roses")
                .withPrice(new BigDecimal(50))
                .withCreationDate(LocalDate.of(2019, 1, 1))
                .withModificationDate(LocalDate.of(2019, 6, 1))
                .withExpirationDate(LocalDate.of(2021, 1, 1))
                .withTags(Set.of())
                .build();

        giftCertificateDTO = new GiftCertificateDTO.Builder()
                .withId(4L)
                .withName("flowers")
                .withDescription("one hundred roses")
                .withPrice(new BigDecimal(50))
                .withCreationDate(LocalDate.of(2019, 1, 1))
                .withModificationDate(LocalDate.of(2019, 6, 1))
                .withExpirationDate(LocalDate.of(2021, 1, 1))
                .withTags(Set.of())
                .build();
    }

    @Test
    public void findAll() {

        List<GiftCertificate> certificates = List.of(giftCertificate);

        Mockito.when(repository.findAll()).thenReturn(certificates);

        List<GiftCertificateDTO> dtos = List.of(converter.convert(giftCertificate));

        assertEquals(dtos, service.findAll());

    }

    @Test
    public void findOneById() {
        Mockito.when(repository.findOne(4)).thenReturn(Optional.of(giftCertificate));

        Optional<GiftCertificateDTO> dto = Optional.of(giftCertificateDTO);

        Optional<GiftCertificateDTO> actual = service.findOne(giftCertificate.getId());
        Mockito.verify(repository, times(1)).findOne(4);
        assertEquals(dto, actual);
    }

    @Test
    public void saveSuccessful() {
        Mockito.when(repository.save(any())).thenReturn(Optional.of(giftCertificate));

        Optional<GiftCertificateDTO> actual = service.save(giftCertificateDTO);
        giftCertificateDTO.setId(4L);
        assertEquals(Optional.of(giftCertificateDTO), actual);

    }

    @Test
    public void saveUnSuccessful() {

        Mockito.when(repository.save(any())).thenReturn(Optional.empty());
        Optional<GiftCertificateDTO> actual = service.save(giftCertificateDTO);
        assertEquals(Optional.empty(), actual);

    }

    @Test
    public void updateSuccessful() {


        Mockito.when(repository.update(any())).thenReturn(Optional.of(giftCertificate));
        Optional<GiftCertificateDTO> actual = service.update(giftCertificateDTO, giftCertificateDTO.getId());
        assertEquals(Optional.of(giftCertificateDTO), actual);
    }

    @Test
    public void updateUnSuccessful() {

        Mockito.when(repository.update(any())).thenReturn(Optional.empty());


        Optional<GiftCertificateDTO> actual = service.update(giftCertificateDTO, giftCertificateDTO.getId());
        assertEquals(Optional.empty(), actual);
    }

    @Test
    public void deleteSuccessful() {

        service.delete(giftCertificateDTO.getId());
        Mockito.verify(repository, times(1)).remove(giftCertificateDTO.getId());


    }

    @Test
    public void findByCriteria() {
        List<GiftCertificate> certificates = List.of(giftCertificate);

        Mockito.when(repository.query(any())).thenReturn(certificates);

        List<GiftCertificateDTO> dtos = List.of(converter.convert(giftCertificate));

        SearchCriteriaRequestDTO searchCriteriaRequestDTO = new SearchCriteriaRequestDTO(new HashMap<>());
        SortCriteriaRequestDTO sortCriteriaRequestDTO = new SortCriteriaRequestDTO(new HashMap<>());
        LimitOffsetCriteriaRequestDTO limitOffsetCriteriaRequestDTO = new LimitOffsetCriteriaRequestDTO(new HashMap<>());


        assertEquals(dtos, service.findByCriteria(
                searchCriteriaRequestDTO, sortCriteriaRequestDTO, limitOffsetCriteriaRequestDTO));

    }

}