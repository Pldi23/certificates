package com.epam.esm.service;


import com.epam.esm.converter.TagConverter;
import com.epam.esm.dto.TagDTO;
import com.epam.esm.entity.Tag;
import com.epam.esm.repository.Repository;
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
    private Repository<Tag> tagRepository;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        TagConverter converter = new TagConverter();
        tagServiceImpl = new TagServiceImpl(tagRepository, converter);
    }


    @Test
    public void getTag() {
        Tag tag = new Tag(1L, "expected");
        Mockito.when(tagRepository.findOne(1))
                .thenReturn(Optional.of(tag));

        Optional<TagDTO> expected = Optional.of(new TagDTO(1L, "expected"));

        //test
        Optional<TagDTO> actual = tagServiceImpl.getTag(tag.getId());

        assertEquals(expected, actual);
    }

    @Test
    public void findAll() {
        List<TagDTO> tagDTOS = List.of(new TagDTO(1L, "expected1"), new TagDTO(2L, "expected2"));
        List<Tag> tags = List.of(new Tag(1L, "expected1"), new Tag(2L, "expected2"));

        Mockito.when(tagRepository.query(any())).thenReturn(tags);

        List<TagDTO> actual = tagServiceImpl.findAll();

        assertEquals(tagDTOS, actual);
    }

    @Test
    public void save() {
        TagDTO tagDTO = new TagDTO(1L, "expected");

        Mockito.when(tagRepository.add(any())).thenReturn(Optional.of(new Tag(8L, tagDTO.getTitle())));
        Optional<TagDTO> actual = tagServiceImpl.save(tagDTO);
        Mockito.verify(tagRepository, times(1)).add(new Tag(1L, "expected"));
        assertEquals(Optional.of(new TagDTO(8L, tagDTO.getTitle())), actual);
    }

    @Test
    public void deleteSuccessful() {
        List<Tag> tags = List.of(new Tag(1L, "expected"));

        tagServiceImpl.delete(tags.get(0).getId());
        Mockito.verify(tagRepository, times(1)).remove(tags.get(0).getId());

    }

    @Test
    public void deleteUnsuccessful() {
        Tag tag = new Tag(1L, "expected");

        tagServiceImpl.delete(tag.getId());
        Mockito.verify(tagRepository, times(1)).remove(tag.getId());
    }
}
