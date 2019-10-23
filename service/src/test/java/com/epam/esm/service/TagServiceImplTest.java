package com.epam.esm.service;


import com.epam.esm.converter.TagConverter;
import com.epam.esm.dto.PageAndSortDTO;
import com.epam.esm.dto.TagDTO;
import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.entity.Tag;
import com.epam.esm.repository.AbstractCertificateRepository;
import com.epam.esm.repository.AbstractTagRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
class TagServiceImplTest {


    @InjectMocks
    private TagServiceImpl tagServiceImpl;

    @Mock
    private AbstractTagRepository tagRepository;

    @Mock
    private AbstractCertificateRepository certificateRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        TagConverter converter = new TagConverter();
        tagServiceImpl = new TagServiceImpl(tagRepository, certificateRepository, converter);
    }


    @Test
    void getTag() {
        Tag tag = new Tag(1L, "expected");
        Mockito.when(tagRepository.findById(1))
                .thenReturn(Optional.of(tag));

        TagDTO expected = new TagDTO(1L, "expected");

        //test
        TagDTO actual = tagServiceImpl.findOne(tag.getId());

        assertEquals(expected, actual);
    }

    @Test
    void findAll() {
        List<TagDTO> tagDTOS = List.of(new TagDTO(1L, "expected1"), new TagDTO(2L, "expected2"));
        List<Tag> tags = List.of(new Tag(1L, "expected1"), new Tag(2L, "expected2"));
        PageAndSortDTO pageAndSortDTO = PageAndSortDTO.builder().sortParameter(null).page(1).size(Integer.MAX_VALUE).build();

        Mockito.when(tagRepository.findAll(pageAndSortDTO.getSortParameter(), pageAndSortDTO.getPage(), pageAndSortDTO.getSize())).thenReturn(tags);

        List<TagDTO> actual = tagServiceImpl.findAll(pageAndSortDTO);

        assertEquals(tagDTOS, actual);
    }

    @Test
    void save() {
        TagDTO tagDTO = new TagDTO(1L, "expected");

        Mockito.when(tagRepository.save(any())).thenReturn(new Tag(8L, tagDTO.getTitle()));
        TagDTO actual = tagServiceImpl.save(tagDTO);
        Mockito.verify(tagRepository, times(1)).save(new Tag(1L, "expected"));
        assertEquals(new TagDTO(8L, tagDTO.getTitle()), actual);
    }

    @Test
    void deleteSuccessful() {
        List<Tag> tags = List.of(new Tag(1L, "expected"));

        tagServiceImpl.delete(tags.get(0).getId());
        Mockito.verify(tagRepository, times(1)).deleteById(tags.get(0).getId());

    }

    @Test
    void deleteUnsuccessful() {
        Tag tag = new Tag(1L, "expected");

        tagServiceImpl.delete(tag.getId());
        Mockito.verify(tagRepository, times(1)).deleteById(tag.getId());
    }

    @Test
    void getTagsByCertificate() {
        GiftCertificate certificate = GiftCertificate.builder().id(1L).name("n").build();
        Tag tag = Tag.builder().title("t").build();
        Mockito.when(certificateRepository.findById(1, true)).thenReturn(Optional.of(certificate));
        Mockito.when(tagRepository.findTagsByCertificate(1, null, 1, Integer.MAX_VALUE)).thenReturn(List.of(tag));
        List<TagDTO> actual = tagServiceImpl.getTagsByCertificate(certificate.getId(),
                PageAndSortDTO.builder().sortParameter(null).page(1).size(Integer.MAX_VALUE).build());
        List<TagDTO> expected = List.of(TagDTO.builder().title("t").build());
        assertEquals(expected, actual);
    }

    @Test
    void findPaginated() {
        Tag tag = Tag.builder().title("t").build();
        Mockito.when(tagRepository.findPaginated( null, 1, Integer.MAX_VALUE)).thenReturn(List.of(tag));
        List<TagDTO> actual = tagServiceImpl.findPaginated(PageAndSortDTO.builder().sortParameter(null).page(1).size(Integer.MAX_VALUE).build());
        List<TagDTO> expected = List.of(TagDTO.builder().title("t").build());
        assertEquals(expected, actual);

    }

    @Test
    void findMostCostEffectiveTag() {
        Tag tag = Tag.builder().title("t").build();
        Mockito.when(tagRepository.findMostCostEffectiveTagByUser(1)).thenReturn(List.of(tag));
        List<TagDTO> actual = tagServiceImpl.findMostCostEffectiveTag(1L);
        List<TagDTO> expected = List.of(TagDTO.builder().title("t").build());
        assertEquals(expected, actual);
    }

    @Test
    void findTagsByOrder() {
        Tag tag = Tag.builder().title("t").build();
        Mockito.when(tagRepository.findTagsByOrder(1, null, 1, Integer.MAX_VALUE)).thenReturn(List.of(tag));
        List<TagDTO> actual = tagServiceImpl.findTagsByOrder(1, PageAndSortDTO.builder().sortParameter(null).page(1).size(Integer.MAX_VALUE).build());
        List<TagDTO> expected = List.of(TagDTO.builder().title("t").build());
        assertEquals(expected, actual);
    }

    @Test
    void findTagsByUser() {
        Tag tag = Tag.builder().title("t").build();
        Mockito.when(tagRepository.findTagsByUserWithCriteria(1, null, 1, Integer.MAX_VALUE)).thenReturn(List.of(tag));
        List<TagDTO> actual = tagServiceImpl.findTagsByUser(1, PageAndSortDTO.builder().sortParameter(null).page(1).size(Integer.MAX_VALUE).build());
        List<TagDTO> expected = List.of(TagDTO.builder().title("t").build());
        assertEquals(expected, actual);
    }

    @Test
    void findPopular() {
        Tag tag = Tag.builder().title("t").build();
        Mockito.when(tagRepository.findPopulars(1, Integer.MAX_VALUE)).thenReturn(List.of(tag));
        List<TagDTO> actual = tagServiceImpl.findPopular(PageAndSortDTO.builder().sortParameter(null).page(1).size(Integer.MAX_VALUE).build());
        List<TagDTO> expected = List.of(TagDTO.builder().title("t").build());
        assertEquals(expected, actual);
    }
}
