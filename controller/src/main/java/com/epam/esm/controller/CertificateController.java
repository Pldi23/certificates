package com.epam.esm.controller;

import com.epam.esm.parser.DtoParser;
import com.epam.esm.service.CertificateService;
import com.epam.esm.service.TagServiceImpl;
import com.epam.esm.validator.RequestParametersValidator;
import com.epam.esm.dto.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.hateoas.EntityLinks;
import org.springframework.hateoas.ExposesResourceFor;
import org.springframework.hateoas.LinkBuilder;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.DataBinder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Min;

import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * gift certificates
 *
 * @author Dzmitry Platonov on 2019-09-26.
 * @version 0.0.1
 */
@RestController
@RequestMapping("certificates")
@Validated
@ExposesResourceFor(GiftCertificateDTO.class)
public class CertificateController {

    private CertificateService certificateServiceImpl;
    private TagServiceImpl tagServiceImpl;
    private DtoParser dtoParser;
    private RequestParametersValidator validator;
    private EntityLinks entityLinks;
    private ResourceBundleMessageSource messageSource;

    @Autowired
    public CertificateController(CertificateService certificateServiceImpl, DtoParser dtoParser,
                                 RequestParametersValidator validator, TagServiceImpl tagServiceImpl,
                                 EntityLinks entityLinks, ResourceBundleMessageSource messageSource) {
        this.certificateServiceImpl = certificateServiceImpl;
        this.dtoParser = dtoParser;
        this.validator = validator;
        this.tagServiceImpl = tagServiceImpl;
        this.entityLinks = entityLinks;
        this.messageSource = messageSource;
    }

    @GetMapping(value = "/")
    public ResponseEntity findAll() {
        List<GiftCertificateDTO> giftCertificateDTOS = certificateServiceImpl.findAll();
        return !giftCertificateDTOS.isEmpty() ? ResponseEntity.ok().body(giftCertificateDTOS)
                : ResponseEntity.notFound().build();
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity findOneById(@PathVariable("id") @Min(0) long id) {
        Optional<GiftCertificateDTO> optionalGiftCertificateDTO = certificateServiceImpl.findOne(id);
        return optionalGiftCertificateDTO.isPresent() ? ResponseEntity.ok().body(optionalGiftCertificateDTO.get())
                : ResponseEntity.notFound().build();
    }

    @PostMapping
    public ResponseEntity add(@Valid @RequestBody GiftCertificateDTO giftCertificateDTO) {
        ResponseEntity responseEntity;

        Optional<GiftCertificateDTO> optionalGiftCertificateDTO = certificateServiceImpl.save(giftCertificateDTO);
        if (optionalGiftCertificateDTO.isPresent()) {
            LinkBuilder linkBuilder
                    = entityLinks.linkForSingleResource(GiftCertificateDTO.class, optionalGiftCertificateDTO.get().getId());
            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.add(HttpHeaders.LOCATION, linkBuilder.toString());
            responseEntity = ResponseEntity.status(201).headers(httpHeaders).body(optionalGiftCertificateDTO.get());
        } else {
            responseEntity = ResponseEntity.status(204).build();
        }
        return responseEntity;
    }

    @PutMapping(value = "/{id}")
    public ResponseEntity update(
            @Valid @RequestBody GiftCertificateDTO giftCertificateDTO,
            @PathVariable("id") @Min(0) long id) {
        MessageDTO messageDTO = certificateServiceImpl.update(giftCertificateDTO, id) ?
                new MessageDTO(messageSource.getMessage("entity.update", null, null), 200) :
                new MessageDTO(messageSource.getMessage("entity.no", null, null), 404);
        return ResponseEntity.status(messageDTO.getStatus()).body(messageDTO);
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity delete(@PathVariable("id") @Min(0) long id) {
        certificateServiceImpl.delete(id);
        return ResponseEntity.status(204).build();
    }

    @GetMapping
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
        return ResponseEntity.ok(certificateServiceImpl
                .findByCriteria(searchCriteriaRequestDTO, sortCriteriaRequestDTO, limitOffsetCriteriaRequestDTO));

    }

    @GetMapping(value = "/{id}/tags")
    public ResponseEntity getTagsByCertificate(@PathVariable("id") @Min(0) long id) {
        return ResponseEntity.ok(tagServiceImpl.getTagsByCertificate(id));
    }
}
