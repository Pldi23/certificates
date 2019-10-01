package com.epam.esm.service;

import com.epam.esm.converter.CertificateConverter;
import com.epam.esm.converter.ConverterFactory;
import com.epam.esm.converter.CriteriaConverter;
import com.epam.esm.dto.*;
import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.repository.AbstractCertificateRepository;
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
public class CertificateServiceTest {

    @InjectMocks
    private CertificateService service;

    @Mock
    private AbstractCertificateRepository repository;

    private CertificateConverter converter;
    private GiftCertificate giftCertificate;
    private GiftCertificateDTO giftCertificateDTO;
    private ResourceBundleMessageSource messageSource;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        converter = new CertificateConverter();
        ConverterFactory converterFactory = new ConverterFactory();
        CriteriaConverter criteriaConverter = new CriteriaConverter(converterFactory);
        messageSource = new ResourceBundleMessageSource();
        messageSource.setBasenames("message");
        messageSource.setUseCodeAsDefaultMessage(true);
        service = new CertificateService(repository, converter, criteriaConverter, messageSource);

        giftCertificate = new GiftCertificate(4,"flowers", "one hundred roses",
                new BigDecimal(50),
                LocalDate.of(2019,1,1), LocalDate.of(2019,6,1),
                LocalDate.of(2021, 1,1), Set.of());

        giftCertificateDTO = new GiftCertificateDTO(4,"flowers", "one hundred roses",
                new BigDecimal(50),
                LocalDate.of(2019,1,1), LocalDate.of(2019,6,1),
                LocalDate.of(2021, 1,1), Set.of());
    }

    @Test
    public void findAll() {

        List<GiftCertificate> certificates = List.of(giftCertificate);

        Mockito.when(repository.query(any())).thenReturn(certificates);

        List<GiftCertificateDTO> dtos = List.of(converter.convert(giftCertificate));

        assertEquals(dtos, service.findAll());

    }

    @Test
    public void findOneById() {

        List<GiftCertificate> certificates = List.of(giftCertificate);

        Mockito.when(repository.query(any())).thenReturn(certificates);

        List<GiftCertificateDTO> dtos = List.of(converter.convert(giftCertificate));

        assertEquals(dtos, service.findOneById(giftCertificate.getId()));
    }

    @Test
    public void saveSuccessful() {

        Mockito.when(repository.query(any())).thenReturn(List.of());
        Mockito.doNothing().when(repository).add(giftCertificate);

        MessageDTO actual = service.save(giftCertificateDTO);
        MessageDTO expected = new MessageDTO(messageSource.getMessage("entity.save", null, null), 201);
        assertEquals(expected, actual);

    }

    @Test
    public void saveUnSuccessful() {

        Mockito.when(repository.query(any())).thenReturn(List.of(giftCertificate));

        MessageDTO actual = service.save(giftCertificateDTO);
        MessageDTO expected = new MessageDTO(messageSource.getMessage("entity.exist", null, null), 400);
        assertEquals(expected, actual);

    }

    @Test
    public void updateSuccessful() {

        Mockito.when(repository.query(any())).thenReturn(List.of(giftCertificate));
        Mockito.doNothing().when(repository).update(giftCertificate);

        MessageDTO actual = service.update(giftCertificateDTO, giftCertificateDTO.getId());
        MessageDTO expected = new MessageDTO(messageSource.getMessage("entity.update", null, null), 200);
        assertEquals(expected, actual);
    }

    @Test
    public void updateUnSuccessful() {

        Mockito.when(repository.query(any())).thenReturn(List.of());


        MessageDTO actual = service.update(giftCertificateDTO, giftCertificateDTO.getId());
        MessageDTO expected = new MessageDTO(messageSource.getMessage("entity.no", null, null), 404);
        assertEquals(expected, actual);
    }

    @Test
    public void deleteSuccessful() {

        MessageDTO actual = service.delete(giftCertificateDTO.getId());
        MessageDTO expected = new MessageDTO(messageSource.getMessage("entity.remove", null, null), 204);
        assertEquals(expected, actual);
        Mockito.verify(repository, times(1)).removeById(giftCertificateDTO.getId());


    }

    @Test
    public void deleteUnSuccessful() {

        MessageDTO actual = service.delete(giftCertificateDTO.getId());
        MessageDTO expected = new MessageDTO(messageSource.getMessage("entity.remove", null, null), 204);
        assertEquals(expected, actual);
        Mockito.verify(repository, times(1)).removeById(giftCertificateDTO.getId());


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