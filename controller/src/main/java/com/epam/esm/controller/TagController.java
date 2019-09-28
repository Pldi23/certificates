package com.epam.esm.controller;

import com.epam.esm.dto.MessageDTO;
import com.epam.esm.dto.TagDTO;
import com.epam.esm.service.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import javax.validation.Valid;
import javax.validation.constraints.Min;

/**
 * gift certificates
 *
 * @author Dzmitry Platonov on 2019-09-25.
 * @version 0.0.1
 */
@RestController
@EnableWebMvc
@RequestMapping("tags")
@Validated
public class TagController {

    private TagService tagService;

    @Autowired
    public TagController(TagService tagService) {
        this.tagService = tagService;
    }

    @GetMapping(value = "/", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity getAllTags() {
        return ResponseEntity.ok().body(tagService.findAll());
    }

    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity getById(@PathVariable("id") @Min(value = 0, message = "id should be greater than 0") long id) {
        return ResponseEntity.ok().body(tagService.getTag(id));
    }

    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity add(@Valid @RequestBody TagDTO tagDTO) {
        MessageDTO messageDTO = tagService.save(tagDTO);
        return ResponseEntity.status(messageDTO.getStatus()).body(messageDTO.getMessage());
    }

    @DeleteMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity delete(@PathVariable("id") @Min(value = 0, message = "id should be greater than 0") long id) {
        MessageDTO messageDTO = tagService.delete(id);
        return ResponseEntity.status(messageDTO.getStatus()).body(messageDTO.getMessage());
    }

//    @GetMapping(value = "/certificates/{id}/tags/{id}")
}
