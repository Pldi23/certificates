package com.epam.esm.controller;

import com.epam.esm.dto.MessageDTO;
import com.epam.esm.dto.TagDTO;
import com.epam.esm.service.CertificateService;
import com.epam.esm.service.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import javax.validation.Valid;
import javax.validation.constraints.Min;
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
@Validated
public class TagController {

    private TagService tagService;
    private CertificateService certificateService;


    @Autowired
    public TagController(TagService tagService, CertificateService certificateService) {
        this.tagService = tagService;
        this.certificateService = certificateService;
    }


    @GetMapping(value = "/", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity getAllTags() {
        List<TagDTO> tagDTOS = tagService.findAll();
        return !tagDTOS.isEmpty() ? ResponseEntity.ok().body(tagDTOS) : ResponseEntity.notFound().build();
    }

    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity getById(@PathVariable("id") @Min(0) long id) {
        List<TagDTO> tagDTOS = tagService.getTag(id);
        return !tagDTOS.isEmpty() ? ResponseEntity.ok().body(tagDTOS) : ResponseEntity.notFound().build();
    }

    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity add(@Valid @RequestBody TagDTO tagDTO) {
        MessageDTO messageDTO = tagService.save(tagDTO);
        return ResponseEntity.status(messageDTO.getStatus()).body(messageDTO);
    }

    @DeleteMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity delete(@PathVariable("id") @Min(0) long id) {
        MessageDTO messageDTO = tagService.delete(id);
        return ResponseEntity.status(messageDTO.getStatus()).body(messageDTO);
    }

    @GetMapping(value = "/{id}/certificates", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity getCertificatesByTag(@PathVariable("id") @Min(0) long id) {
        return ResponseEntity.ok(certificateService.getByTag(id));
    }

}
