package com.epam.esm.controller;

import com.epam.esm.dto.MessageDTO;
import com.epam.esm.dto.TagDTO;
import com.epam.esm.service.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import java.util.List;

/**
 * gift certificates
 *
 * @author Dzmitry Platonov on 2019-09-25.
 * @version 0.0.1
 */
@RestController
@EnableWebMvc
@RequestMapping("tags")
public class TagController {

    private TagService tagService;

    @Autowired
    public TagController(TagService tagService) {
        this.tagService = tagService;
    }

    @GetMapping(value = "/", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<TagDTO> getAllTags() {
        return tagService.findAll();
    }

    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<TagDTO> getById(@PathVariable("id") long id) {
        return tagService.getTag(id);
    }

    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public MessageDTO add(@RequestBody TagDTO tagDTO) {
        return tagService.save(tagDTO);
    }

    @DeleteMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public MessageDTO delete(@PathVariable("id") long id) {
        return tagService.delete(id);
    }

//    @GetMapping(value = "/certificates/{id}/tags/{id}")
}
