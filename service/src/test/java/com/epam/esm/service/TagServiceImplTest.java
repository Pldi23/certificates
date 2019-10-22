package com.epam.esm.service;


import com.epam.esm.converter.TagConverter;
import com.epam.esm.dto.PageAndSortDTO;
import com.epam.esm.dto.TagDTO;
import com.epam.esm.entity.Tag;
import com.epam.esm.repository.AbstractCertificateRepository;
import com.epam.esm.repository.AbstractTagRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;

/**
 * gift certificates
 *
 * @author Dzmitry Platonov on 2019-09-29.
 * @version 0.0.1
 */
@RunWith(MockitoJUnitRunner.class)
public class TagServiceImplTest {


    @InjectMocks
    private TagServiceImpl tagServiceImpl;

    @Mock
    private AbstractTagRepository tagRepository;

    @Mock
    private AbstractCertificateRepository certificateRepository;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        TagConverter converter = new TagConverter();
        tagServiceImpl = new TagServiceImpl(tagRepository, certificateRepository, converter);
    }


    @Test
    public void getTag() {
        Tag tag = new Tag(1L, "expected");
        Mockito.when(tagRepository.findById(1))
                .thenReturn(Optional.of(tag));

        TagDTO expected = new TagDTO(1L, "expected");

        //test
        TagDTO actual = tagServiceImpl.findOne(tag.getId());

        assertEquals(expected, actual);
    }

    @Test
    public void findAll() {
        List<TagDTO> tagDTOS = List.of(new TagDTO(1L, "expected1"), new TagDTO(2L, "expected2"));
        List<Tag> tags = List.of(new Tag(1L, "expected1"), new Tag(2L, "expected2"));
        PageAndSortDTO pageAndSortDTO = PageAndSortDTO.builder().sortParameter(null).page(1).size(Integer.MAX_VALUE).build();

        Mockito.when(tagRepository.findAll(pageAndSortDTO.getSortParameter(), pageAndSortDTO.getPage(), pageAndSortDTO.getSize())).thenReturn(tags);

        List<TagDTO> actual = tagServiceImpl.findAll(pageAndSortDTO);

        assertEquals(tagDTOS, actual);
    }

    @Test
    public void save() {
        TagDTO tagDTO = new TagDTO(1L, "expected");

        Mockito.when(tagRepository.save(any())).thenReturn(new Tag(8L, tagDTO.getTitle()));
        TagDTO actual = tagServiceImpl.save(tagDTO);
        Mockito.verify(tagRepository, times(1)).save(new Tag(1L, "expected"));
        assertEquals(new TagDTO(8L, tagDTO.getTitle()), actual);
    }

    @Test
    public void deleteSuccessful() {
        List<Tag> tags = List.of(new Tag(1L, "expected"));

        tagServiceImpl.delete(tags.get(0).getId());
        Mockito.verify(tagRepository, times(1)).deleteById(tags.get(0).getId());

    }

    @Test
    public void deleteUnsuccessful() {
        Tag tag = new Tag(1L, "expected");

        tagServiceImpl.delete(tag.getId());
        Mockito.verify(tagRepository, times(1)).deleteById(tag.getId());
    }
}
