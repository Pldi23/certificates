package com.epam.esm.service;

import com.epam.esm.converter.CertificateConverter;
import com.epam.esm.converter.CriteriaConverter;
import com.epam.esm.converter.CriteriaCreatorHelper;
import com.epam.esm.converter.TagConverter;
import com.epam.esm.dto.GiftCertificateDTO;

import com.epam.esm.entity.GiftCertificate;

import com.epam.esm.repository.AbstractCertificateRepository;
import com.epam.esm.repository.AbstractTagRepository;
import com.epam.esm.util.Translator;
import com.epam.esm.validator.ExpirationDateValidator;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.support.ResourceBundleMessageSource;

import java.math.BigDecimal;
import java.time.LocalDate;

import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;

/**
 * gift certificates
 *
 * @author Dzmitry Platonov on 2019-09-29.
 * @version 0.0.1
 */
@ExtendWith(MockitoExtension.class)
public class CertificateServiceImplTest {

    @InjectMocks
    private CertificateServiceImpl service;

    @InjectMocks
    private Translator translator;

    @InjectMocks
    ResourceBundleMessageSource messageSource;

    @Mock
    private AbstractCertificateRepository certificateRepository;

    @Mock
    private AbstractTagRepository tagRepository;

    private CertificateConverter converter;
    private GiftCertificate giftCertificate;
    private GiftCertificateDTO giftCertificateDTO;
    private TagConverter tagConverter;


    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        converter = new CertificateConverter();
        CriteriaCreatorHelper helper = new CriteriaCreatorHelper();
        CriteriaConverter criteriaConverter = new CriteriaConverter(helper);
        ExpirationDateValidator validator = new ExpirationDateValidator();
        messageSource = new ResourceBundleMessageSource();

        translator = new Translator(messageSource);
        tagConverter = new TagConverter();
        service = new CertificateServiceImpl(certificateRepository, tagRepository, converter, criteriaConverter, validator, tagConverter);

        giftCertificate = GiftCertificate.builder()
                .id(4L)
                .name("flowers")
                .description("one hundred roses")
                .price(new BigDecimal(50))
                .creationDate(LocalDate.of(2019, 1, 1))
                .modificationDate(LocalDate.of(2019, 6, 1))
                .expirationDate(LocalDate.of(2021, 1, 1))
                .tags(Set.of())
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

//    @Test
//    public void findAll() {
//
//        List<GiftCertificate> certificates = List.of(giftCertificate);
//        PageAndSortDTO pageAndSortDTO = PageAndSortDTO.builder().sortParameter(null).page(1).size(Integer.MAX_VALUE).build();
//
//        Mockito.when(certificateRepository.findAll(pageAndSortDTO.getSortParameter(), pageAndSortDTO.getPage(), pageAndSortDTO.getSize(), false)).thenReturn(certificates);
//
//        List<GiftCertificateDTO> dtos = List.of(converter.convert(giftCertificate));
//
//        assertEquals(dtos, service.findAll(pageAndSortDTO));
//
//    }

    @Test
    public void findOneById() {
        Mockito.when(certificateRepository.findById(4, true)).thenReturn(Optional.of(giftCertificate));

        GiftCertificateDTO dto = giftCertificateDTO;

        GiftCertificateDTO actual = service.findOne(giftCertificate.getId());
        Mockito.verify(certificateRepository, times(1)).findById(4, true);
        assertEquals(dto, actual);
    }

    @Test
    public void saveSuccessful() {
        Mockito.when(certificateRepository.save(any())).thenReturn(giftCertificate);

        GiftCertificateDTO actual = service.save(giftCertificateDTO);
        giftCertificateDTO.setId(4L);
        assertEquals(giftCertificateDTO, actual);

    }

//    @Test(expected = EntityAlreadyExistsException.class)
//    public void saveUnSuccessful() {
//
//        Mockito.when(certificateRepository.save(any())).thenThrow(DataIntegrityViolationException.class);
//        service.save(giftCertificateDTO);
//
//    }
//
//    @Test
//    public void updateSuccessful() {
//
//
//        Mockito.when(certificateRepository.save(any())).thenReturn(giftCertificate);
//        GiftCertificateDTO actual = service.update(giftCertificateDTO, giftCertificateDTO.getId());
//        assertEquals(giftCertificateDTO, actual);
//    }
//
//    @Test
//    public void updateUnSuccessful() {
//
//        Mockito.when(certificateRepository.save(any())).thenReturn(null);
//
//
//        GiftCertificateDTO actual = service.update(giftCertificateDTO, giftCertificateDTO.getId());
//        assertEquals(null, actual);
//    }

    @Test
    public void deleteSuccessful() {

        service.delete(giftCertificateDTO.getId());
        Mockito.verify(certificateRepository, times(1)).deleteById(giftCertificateDTO.getId());


    }

//    @Test
//    public void findByCriteria() {
//        List<GiftCertificate> certificates = List.of(giftCertificate);
//        PageAndSortDTO pageAndSortDTO = PageAndSortDTO.builder().sortParameter(null).page(1).size(Integer.MAX_VALUE).build();
//        SearchCriteria searchCriteria = new SearchCriteria.Builder().build();
//
//        Mockito.when(certificateRepository.findByCriteria(searchCriteria, pageAndSortDTO.getSortParameter(), pageAndSortDTO.getPage(), pageAndSortDTO.getSize())).thenReturn(certificates);
//
//        List<GiftCertificateDTO> dtos = List.of(converter.convert(giftCertificate));
//
//        SearchCriteriaRequestDTO searchCriteriaRequestDTO = new SearchCriteriaRequestDTO(new HashMap<>());
//        SortCriteriaRequestDTO sortCriteriaRequestDTO = new SortCriteriaRequestDTO(new HashMap<>());
//        LimitOffsetCriteriaRequestDTO limitOffsetCriteriaRequestDTO = new LimitOffsetCriteriaRequestDTO(new HashMap<>());
//
//
//        assertEquals(dtos, service.findByCriteria(searchCriteriaRequestDTO, pageAndSortDTO));
//
//    }

}