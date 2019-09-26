package com.epam.esm.service;

import com.epam.esm.converter.TagConverter;
import com.epam.esm.dto.MessageDTO;
import com.epam.esm.dto.TagDTO;
import com.epam.esm.entity.Tag;
import com.epam.esm.repository.Repository;
import com.epam.esm.specification.FindAllTagSpecification;
import com.epam.esm.specification.FindTagByTitlePartSpecification;
import com.epam.esm.specification.FindTagByIdSpecification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * gift certificates
 *
 * @author Dzmitry Platonov on 2019-09-25.
 * @version 0.0.1
 */
@Service(value = "TagService")
@Transactional
public class TagService {

    private static final String TAG_ADDED = "successfully added";
    private static final String TAG_EXISTS = "already exists";


    @Qualifier(value = "TagRepository")
    private Repository<Tag> tagRepository;

    private TagConverter tagConverter;

    @Autowired
    public TagService(Repository<Tag> tagRepository, TagConverter tagConverter) {
        this.tagRepository = tagRepository;
        this.tagConverter = tagConverter;
    }

    public List<TagDTO> getTag(String title) {
        return tagRepository.query(new FindTagByTitlePartSpecification(title)).stream()
                .map(tag -> tagConverter.convert(tag))
                .collect(Collectors.toList());
    }

    public List<TagDTO> getTag(long id) {
        return tagRepository.query(new FindTagByIdSpecification(id)).stream()
                .map(tag -> tagConverter.convert(tag))
                .collect(Collectors.toList());
    }

    public List<TagDTO> findAll() {
        return tagRepository.query(new FindAllTagSpecification()).stream()
                .map(tag -> tagConverter.convert(tag))
                .collect(Collectors.toList());
    }

    public MessageDTO save(TagDTO tagDTO) {
        Tag tag = tagConverter.convert(tagDTO);
        String message;
        if (tagRepository.query(new FindTagByIdSpecification(tag.getId())).isEmpty()) {
            tagRepository.add(tag);
            message = TAG_ADDED;
        } else {
            message = TAG_EXISTS;
        }
        return new MessageDTO(message);
    }

    public MessageDTO delete(long id) {
        List<Tag> tags = tagRepository.query(new FindTagByIdSpecification(id));
        String message;
        if (!tags.isEmpty()) {
            tagRepository.remove(tags.get(0));
            message = TAG_ADDED;
        } else {
            message = "tag with id: " + id + " not found";
        }
        return new MessageDTO(message);
    }
}
