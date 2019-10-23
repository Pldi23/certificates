package com.epam.esm.service;

import com.epam.esm.converter.CertificateConverter;
import com.epam.esm.converter.CriteriaConverter;
import com.epam.esm.converter.CriteriaCreatorHelper;
import com.epam.esm.converter.TagConverter;
import com.epam.esm.dto.CertificatePatchDTO;
import com.epam.esm.dto.GiftCertificateDTO;

import com.epam.esm.dto.PageAndSortDTO;
import com.epam.esm.dto.SearchCriteriaRequestDTO;
import com.epam.esm.entity.GiftCertificate;

import com.epam.esm.entity.Order;
import com.epam.esm.entity.Tag;
import com.epam.esm.entity.criteria.SearchCriteria;
import com.epam.esm.repository.AbstractCertificateRepository;
import com.epam.esm.repository.AbstractTagRepository;
import com.epam.esm.validator.ExpirationDateValidator;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
class CertificateServiceImplTest {

    @InjectMocks
    private CertificateServiceImpl service;

    @Mock
    private AbstractCertificateRepository certificateRepository;

    @Mock
    private AbstractTagRepository tagRepository;

    private GiftCertificate giftCertificate;
    private GiftCertificateDTO giftCertificateDTO;
    private CriteriaConverter criteriaConverter;


    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        CertificateConverter converter = new CertificateConverter();
        CriteriaCreatorHelper helper = new CriteriaCreatorHelper();
        criteriaConverter = new CriteriaConverter(helper);
        ExpirationDateValidator validator = new ExpirationDateValidator();
        TagConverter tagConverter = new TagConverter();
        service = new CertificateServiceImpl(certificateRepository, tagRepository, converter,
                criteriaConverter, validator, tagConverter);

        giftCertificate = GiftCertificate.builder()
                .id(4L)
                .name("flowers")
                .description("one hundred roses")
                .price(new BigDecimal(50))
                .creationDate(LocalDate.of(2019, 1, 1))
                .modificationDate(LocalDate.now())
                .expirationDate(LocalDate.of(2021, 1, 1))
                .tags(Set.of())
                .build();

        giftCertificateDTO = new GiftCertificateDTO.Builder()
                .withId(4L)
                .withName("flowers")
                .withDescription("one hundred roses")
                .withPrice(new BigDecimal(50))
                .withCreationDate(LocalDate.of(2019, 1, 1))
                .withModificationDate(LocalDate.now())
                .withExpirationDate(LocalDate.of(2021, 1, 1))
                .withTags(Set.of())
                .build();
    }

    @Test
    void findAll() {

        List<GiftCertificate> certificates = List.of(giftCertificate);
        PageAndSortDTO pageAndSortDTO = PageAndSortDTO.builder().sortParameter(null).page(1).size(Integer.MAX_VALUE).build();

        Mockito.when(certificateRepository
                .findAll(pageAndSortDTO.getSortParameter(), pageAndSortDTO.getPage(), pageAndSortDTO.getSize(), true))
                .thenReturn(certificates);

        List<GiftCertificateDTO> dtos = List.of(giftCertificateDTO);

        assertEquals(dtos, service.findAll(pageAndSortDTO));

    }

    @Test
    void findOneById() {
        Mockito.when(certificateRepository.findById(4, true)).thenReturn(Optional.of(giftCertificate));

        GiftCertificateDTO dto = giftCertificateDTO;

        GiftCertificateDTO actual = service.findOne(giftCertificate.getId());
        Mockito.verify(certificateRepository, times(1)).findById(4, true);
        assertEquals(dto, actual);
    }

    @Test
    void saveSuccessful() {
        Mockito.when(certificateRepository.save(any())).thenReturn(giftCertificate);

        GiftCertificateDTO actual = service.save(giftCertificateDTO);
        giftCertificateDTO.setId(4L);
        assertEquals(giftCertificateDTO, actual);

    }

    @Test
    void updateSuccessful() {
        Mockito.when(certificateRepository.findById(4, true)).thenReturn(Optional.of(giftCertificate));
        Mockito.when(certificateRepository.save(any())).thenReturn(giftCertificate);
        GiftCertificateDTO actual = service.update(giftCertificateDTO, giftCertificateDTO.getId());
        assertEquals(giftCertificateDTO, actual);
    }

    @Test
    void deleteSuccessful() {

        service.delete(giftCertificateDTO.getId());
        Mockito.verify(certificateRepository, times(1)).deleteById(giftCertificateDTO.getId());


    }

    @Test
    void findByCriteria() {
        List<GiftCertificate> certificates = List.of(giftCertificate);
        PageAndSortDTO pageAndSortDTO = PageAndSortDTO.builder().sortParameter(null).page(1).size(Integer.MAX_VALUE).build();
        SearchCriteriaRequestDTO searchCriteriaRequestDTO = new SearchCriteriaRequestDTO(new HashMap<>());
        SearchCriteria searchCriteria = criteriaConverter.convertSearchCriteria(searchCriteriaRequestDTO);
        Mockito.when(certificateRepository.findByCriteria(searchCriteria, pageAndSortDTO.getSortParameter(),
                pageAndSortDTO.getPage(), pageAndSortDTO.getSize())).thenReturn(certificates);

        List<GiftCertificateDTO> expected = List.of(giftCertificateDTO);
        List<GiftCertificateDTO> actual = service.findByCriteria(searchCriteriaRequestDTO, pageAndSortDTO);


        assertEquals(expected, actual);

    }

    @Test
    void getByTag() {
        Tag tag = Tag.builder().id(1L).title("t").build();
        PageAndSortDTO pageAndSortDTO = PageAndSortDTO.builder().sortParameter(null).page(1).size(Integer.MAX_VALUE).build();

        Mockito.when(tagRepository.findById(1)).thenReturn(Optional.of(tag));
        Mockito.when(certificateRepository.findByTagsId(1, pageAndSortDTO.getSortParameter(),
                pageAndSortDTO.getPage(), pageAndSortDTO.getSize())).thenReturn(List.of(giftCertificate));

        List<GiftCertificateDTO> actual = service.getByTag(1, pageAndSortDTO);
        List<GiftCertificateDTO> expected = List.of(giftCertificateDTO);
        assertEquals(expected, actual);

    }

    @Test
    void findByOrder() {
        Order order = Order.builder().id(1L).build();
        PageAndSortDTO pageAndSortDTO = PageAndSortDTO.builder().sortParameter(null).page(1).size(Integer.MAX_VALUE).build();

        Mockito.when(certificateRepository.findByOrder(order.getId(), pageAndSortDTO.getSortParameter(),
                pageAndSortDTO.getPage(), pageAndSortDTO.getSize())).thenReturn(List.of(giftCertificate));

        List<GiftCertificateDTO> actual = service.findByOrder(order.getId(), pageAndSortDTO);
        List<GiftCertificateDTO> expected = List.of(giftCertificateDTO);
        assertEquals(expected, actual);
    }

    @Test
    void patch() {
        CertificatePatchDTO patchDTO = CertificatePatchDTO.builder()
                .name("n")
                .price(new BigDecimal(1))
                .expirationDate(LocalDate.of(2030,1,1))
                .description("d")
                .tags(new HashSet<>())
                .build();

        Mockito.when(certificateRepository.findById(4, true)).thenReturn(Optional.of(giftCertificate));


        Mockito.when(certificateRepository.save(any())).thenReturn(giftCertificate);
        GiftCertificateDTO actual = service.patch(patchDTO, giftCertificateDTO.getId());
        GiftCertificateDTO expected = new GiftCertificateDTO.Builder()
                .withId(4L)
                .withName("n")
                .withDescription("d")
                .withPrice(new BigDecimal(1))
                .withCreationDate(LocalDate.of(2019, 1, 1))
                .withModificationDate(LocalDate.now())
                .withExpirationDate(LocalDate.of(2030, 1, 1))
                .withTags(Set.of())
                .build();

        assertEquals(expected, actual);
    }
}