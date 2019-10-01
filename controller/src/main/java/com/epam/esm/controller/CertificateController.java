package com.epam.esm.controller;

import com.epam.esm.parser.DtoParser;
import com.epam.esm.service.CertificateService;
import com.epam.esm.service.TagService;
import com.epam.esm.validator.RequestParametersValidator;
import com.epam.esm.dto.*;
import org.springframework.hateoas.EntityLinks;
import org.springframework.hateoas.ExposesResourceFor;
import org.springframework.hateoas.LinkBuilder;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.DataBinder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import javax.validation.Valid;
import javax.validation.constraints.Min;

import java.util.List;
import java.util.Map;

/**
 * gift certificates
 *
 * @author Dzmitry Platonov on 2019-09-26.
 * @version 0.0.1
 */
@RestController
@EnableWebMvc
@RequestMapping("certificates")
@Validated
@ExposesResourceFor(GiftCertificateDTO.class)
public class CertificateController {

    private CertificateService certificateService;
    private TagService tagService;
    private DtoParser dtoParser;
    private RequestParametersValidator validator;
    private EntityLinks entityLinks;



    public CertificateController(CertificateService certificateService, DtoParser dtoParser,
                                 RequestParametersValidator validator, TagService tagService,
                                 EntityLinks entityLinks) {
        this.certificateService = certificateService;
        this.dtoParser = dtoParser;
        this.validator = validator;
        this.tagService = tagService;
        this.entityLinks = entityLinks;
    }

    @GetMapping(value = "/", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity findAll() {
        List<GiftCertificateDTO> giftCertificateDTOS = certificateService.findAll();
        return !giftCertificateDTOS.isEmpty() ? ResponseEntity.ok().body(giftCertificateDTOS)
                : ResponseEntity.notFound().build();
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity findOneById(@PathVariable("id") @Min(0) long id) {
        List<GiftCertificateDTO> giftCertificateDTOS = certificateService.findOneById(id);
        return !giftCertificateDTOS.isEmpty() ? ResponseEntity.ok().body(giftCertificateDTOS)
                : ResponseEntity.notFound().build();
    }

    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity add(@Valid @RequestBody GiftCertificateDTO giftCertificateDTO) {
        ResponseEntity responseEntity;

        GiftCertificateDTO createdGiftCertificatesDTO = certificateService.save(giftCertificateDTO);
        if (createdGiftCertificatesDTO != null) {
            LinkBuilder linkBuilder
                    = entityLinks.linkForSingleResource(GiftCertificateDTO.class, createdGiftCertificatesDTO.getId());
            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.add(HttpHeaders.LOCATION, linkBuilder.toString());
            responseEntity = ResponseEntity.status(201).headers(httpHeaders).body(createdGiftCertificatesDTO);
        } else {
            responseEntity = ResponseEntity.status(500).build();
        }
        return responseEntity;
    }

    @PutMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity update(
            @Valid @RequestBody GiftCertificateDTO giftCertificateDTO,
            @PathVariable("id") @Min(0) long id) {
        MessageDTO messageDTO = certificateService.update(giftCertificateDTO, id);
        return ResponseEntity.status(messageDTO.getStatus()).body(messageDTO);
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity delete(@PathVariable("id") @Min(0) long id) {
        MessageDTO messageDTO = certificateService.delete(id);
        return ResponseEntity.status(messageDTO.getStatus()).body(messageDTO);
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity findByCriteria(@RequestParam Map<String, String> criteriaMap) {
        DataBinder dataBinder = new DataBinder(criteriaMap);
        dataBinder.addValidators(validator);
        dataBinder.validate();
        if (dataBinder.getBindingResult().hasErrors()) {
            return ResponseEntity.badRequest().body(dataBinder.getBindingResult().getAllErrors());
        }
            SortCriteriaRequestDTO sortCriteriaRequestDTO = dtoParser.parseSortCriteria(criteriaMap);
            SearchCriteriaRequestDTO searchCriteriaRequestDTO = dtoParser.parseSearchCriteria(criteriaMap);
            LimitOffsetCriteriaRequestDTO limitOffsetCriteriaRequestDTO = dtoParser.parseLimitOffsetCriteria(criteriaMap);
            return ResponseEntity.ok(certificateService
                    .findByCriteria(searchCriteriaRequestDTO, sortCriteriaRequestDTO, limitOffsetCriteriaRequestDTO));

    }

    @GetMapping(value = "/{id}/tags", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity getTagsByCertificate(@PathVariable("id") @Min(0) long id) {
        return ResponseEntity.ok(tagService.getTagsByCertificate(id));
    }
}
