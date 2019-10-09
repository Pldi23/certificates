package com.epam.esm.controller;

import com.epam.esm.dto.GiftCertificateDTO;
import com.epam.esm.dto.TagDTO;
import com.epam.esm.dto.ViolationDTO;
import com.epam.esm.service.CertificateService;
import com.epam.esm.service.TagService;
import org.springframework.context.MessageSource;
import org.springframework.hateoas.EntityLinks;
import org.springframework.hateoas.ExposesResourceFor;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.LinkBuilder;
import org.springframework.hateoas.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.validation.beanvalidation.SpringValidatorAdapter;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;

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
    private CertificateService certificateService;
    private EntityLinks entityLinks;
    private SpringValidatorAdapter localValidatorFactoryBean;
    private MessageSource messageSource;



    public TagController(TagService tagServiceImpl, CertificateService certificateServiceImpl, EntityLinks entityLinks,
                         SpringValidatorAdapter localValidatorFactoryBean, MessageSource messageSource) {
        this.tagServiceImpl = tagServiceImpl;
        this.certificateService = certificateServiceImpl;
        this.entityLinks = entityLinks;
        this.localValidatorFactoryBean = localValidatorFactoryBean;
        this.messageSource = messageSource;
    }

    @InitBinder
    protected void initBinder(WebDataBinder binder) {
        binder.setValidator(localValidatorFactoryBean);
    }


    @GetMapping
    public ResponseEntity getAllTags() {
        List<TagDTO> tagDTOS = tagServiceImpl.findAll();
        if (!tagDTOS.isEmpty()) {
            List<Resource> resources = new ArrayList<>(tagDTOS.size());
            tagDTOS.forEach(tagDTO -> resources.add(tagToResource(tagDTO)));
            return ResponseEntity.ok().body(resources);
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity getById(@PathVariable("id")
                                  @Min(value = 0, message = "{violation.id}") long id) {

        Optional<TagDTO> optionalTagDTO = tagServiceImpl.findOne(id);
        return optionalTagDTO.isPresent() ?
                ResponseEntity.ok().body(
                        tagToResource(optionalTagDTO.get())) :
                ResponseEntity.notFound().build();
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
    public ResponseEntity delete(@PathVariable("id") @Min(value = 0, message = "{violation.id}") long id) {
        return tagServiceImpl.delete(id) ? ResponseEntity.status(204).build() :
                ResponseEntity.status(404).body(new ViolationDTO(
                        List.of(messageSource.getMessage("entity.no.tag", null, null)), 404, LocalDateTime.now()));
    }

    @GetMapping(value = "/{id}/certificates")
    public ResponseEntity getCertificatesByTag(@PathVariable("id") @Min(value = 0, message = "{violation.id}") long id) {
        List<GiftCertificateDTO> giftCertificateDTOS = certificateService.getByTag(id);
        if (!giftCertificateDTOS.isEmpty()) {
            List<Resource> resources = new ArrayList<>(giftCertificateDTOS.size());
            giftCertificateDTOS.forEach(giftCertificateDTO ->
                resources.add(certificateToResource(giftCertificateDTO)));

            return ResponseEntity.ok(resources);
        }
        return ResponseEntity.ok(certificateService.getByTag(id));
    }

    private Resource tagToResource(TagDTO tagDTO) {
        return new Resource<>(tagDTO,
                linkTo(TagController.class).slash(tagDTO.getId()).withSelfRel());
    }

    private Resource<GiftCertificateDTO> certificateToResource(GiftCertificateDTO giftCertificateDTO) {
        List<Link> links = new ArrayList<>(giftCertificateDTO.getTags().size() + 1);
        links.add(linkTo(CertificateController.class).slash(giftCertificateDTO.getId()).withSelfRel());
        giftCertificateDTO.getTags().forEach(tagDTO -> links.add(linkTo(TagController.class).slash(tagDTO.getId()).withRel("tag")));
        return new Resource<>(giftCertificateDTO, links);
    }

}
