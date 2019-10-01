package com.epam.esm.service;


import com.epam.esm.converter.TagConverter;
import com.epam.esm.dto.MessageDTO;
import com.epam.esm.dto.TagDTO;
import com.epam.esm.entity.Tag;
import com.epam.esm.repository.AbstractTagRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.context.support.ResourceBundleMessageSource;

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
public class TagServiceTest {


    @InjectMocks
    private TagService tagService;

    @Mock
    private AbstractTagRepository tagRepository;
    private ResourceBundleMessageSource messageSource;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        TagConverter converter = new TagConverter();
        messageSource = new ResourceBundleMessageSource();
        messageSource.setBasenames("message");
        messageSource.setUseCodeAsDefaultMessage(true);
        tagService = new TagService(tagRepository, converter, messageSource);
    }


    @Test
    public void getTag() {
        Tag tag = new Tag(1, "expected");
        Mockito.when(tagRepository.query(any()))
                .thenReturn(List.of(tag));

        List<TagDTO> expected = List.of(new TagDTO(1, "expected"));

        //test
        List<TagDTO> actual = tagService.getTag(tag.getId());

        assertEquals(expected, actual);
    }

    @Test
    public void findAll() {
        List<TagDTO> tagDTOS = List.of(new TagDTO(1, "expected1"), new TagDTO(2, "expected2"));
        List<Tag> tags = List.of(new Tag(1, "expected1"), new Tag(2, "expected2"));

        Mockito.when(tagRepository.query(any())).thenReturn(tags);

        List<TagDTO> actual = tagService.findAll();

        assertEquals(tagDTOS, actual);
    }

    @Test
    public void save() {
        TagDTO tagDTO = new TagDTO(1, "expected");

        Mockito.when(tagRepository.add(any())).thenReturn(Optional.of(new Tag(8, tagDTO.getTitle())));
        Optional<TagDTO> actual = tagService.save(tagDTO);
        Mockito.verify(tagRepository, times(1)).add(new Tag(1, "expected"));
        assertEquals(Optional.of(new TagDTO(8, tagDTO.getTitle())), actual);
    }

    @Test
    public void deleteSuccessful() {
        List<Tag> tags = List.of(new Tag(1, "expected"));

        MessageDTO expected = new MessageDTO(messageSource.getMessage("entity.remove", null, null), 204);

        assertEquals(expected, tagService.delete(1));
        Mockito.verify(tagRepository, times(1)).removeById(tags.get(0).getId());

    }

    @Test
    public void deleteUnsuccessful() {
        Tag tag = new Tag(1, "expected");

        MessageDTO expected = new MessageDTO(messageSource.getMessage("entity.remove", null, null), 204);
        assertEquals(expected, tagService.delete(1));
        Mockito.verify(tagRepository, times(1)).removeById(tag.getId());
    }
}
