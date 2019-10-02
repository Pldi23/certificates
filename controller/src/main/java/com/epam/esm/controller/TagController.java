package com.epam.esm.controller;

import com.epam.esm.dto.TagDTO;
import com.epam.esm.service.CertificateServiceImpl;
import com.epam.esm.service.TagService;
import org.springframework.hateoas.EntityLinks;
import org.springframework.hateoas.ExposesResourceFor;
import org.springframework.hateoas.LinkBuilder;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import java.util.List;
import java.util.Optional;

/**
 * gift certificates
 *
 * @author Dzmitry Platonov on 2019-09-25.
 * @version 0.0.1
 */
@RestController
@RequestMapping("tags")
@Validated
@ExposesResourceFor(TagDTO.class)
public class TagController {

    private TagService tagServiceImpl;
    private CertificateServiceImpl certificateServiceImpl;
    private EntityLinks entityLinks;

    public TagController(TagService tagServiceImpl, CertificateServiceImpl certificateServiceImpl, EntityLinks entityLinks) {
        this.tagServiceImpl = tagServiceImpl;
        this.certificateServiceImpl = certificateServiceImpl;
        this.entityLinks = entityLinks;
    }


    @GetMapping(value = "/")
    public ResponseEntity getAllTags() {
        List<TagDTO> tagDTOS = tagServiceImpl.findAll();
        return !tagDTOS.isEmpty() ? ResponseEntity.ok().body(tagDTOS) : ResponseEntity.notFound().build();
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity getById(@PathVariable("id") @Min(0) long id) {
        Optional<TagDTO> tagDTOS = tagServiceImpl.getTag(id);
        return tagDTOS.isPresent() ? ResponseEntity.ok().body(tagDTOS.get()) : ResponseEntity.notFound().build();
    }

    @PostMapping
    public ResponseEntity add(@Valid @RequestBody TagDTO tagDTO) {
        ResponseEntity responseEntity;
        Optional<TagDTO> optionalTagDTO = tagServiceImpl.save(tagDTO);
        if (optionalTagDTO.isPresent()) {
            LinkBuilder linkBuilder
                    = entityLinks.linkForSingleResource(TagDTO.class, optionalTagDTO.get().getId());
            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.add(HttpHeaders.LOCATION, linkBuilder.toString());
            responseEntity = ResponseEntity.status(201).headers(httpHeaders).body(optionalTagDTO.get());
        } else {
            responseEntity = ResponseEntity.status(204).build();
        }
        return responseEntity;
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity delete(@PathVariable("id") @Min(0) long id) {
        tagServiceImpl.delete(id);
        return ResponseEntity.status(204).build();
    }

    @GetMapping(value = "/{id}/certificates")
    public ResponseEntity getCertificatesByTag(@PathVariable("id") @Min(0) long id) {
        return ResponseEntity.ok(certificateServiceImpl.getByTag(id));
    }

}
