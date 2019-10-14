package com.epam.esm.controller;

import com.epam.esm.hateoas.LinkCreator;
import com.epam.esm.parser.DtoParser;
import com.epam.esm.service.CertificateService;
import com.epam.esm.service.TagService;
import com.epam.esm.validator.RequestParametersValidator;
import com.epam.esm.dto.*;
import org.springframework.context.MessageSource;
import org.springframework.hateoas.EntityLinks;
import org.springframework.hateoas.ExposesResourceFor;
import org.springframework.hateoas.LinkBuilder;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.DataBinder;
import org.springframework.validation.Validator;
import org.springframework.validation.annotation.Validated;
import org.springframework.validation.beanvalidation.SpringValidatorAdapter;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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
    private TagService tagService;
    private DtoParser dtoParser;
    private Validator validator;
    private EntityLinks entityLinks;
    private MessageSource messageSource;
    private SpringValidatorAdapter localValidatorFactoryBean;
    private LinkCreator linkCreator;


    public CertificateController(CertificateService certificateServiceImpl, DtoParser dtoParser,
                                 RequestParametersValidator validator, TagService tagServiceImpl,
                                 EntityLinks entityLinks, MessageSource messageSource,
                                 SpringValidatorAdapter localValidatorFactoryBean,
                                 LinkCreator linkCreator) {
        this.certificateServiceImpl = certificateServiceImpl;
        this.dtoParser = dtoParser;
        this.validator = validator;
        this.tagService = tagServiceImpl;
        this.entityLinks = entityLinks;
        this.messageSource = messageSource;
        this.localValidatorFactoryBean = localValidatorFactoryBean;
        this.linkCreator = linkCreator;
    }

    @InitBinder
    protected void initBinder(WebDataBinder binder) {
        binder.setValidator(localValidatorFactoryBean);
    }

    @GetMapping(value = "/")
    public ResponseEntity findAll() {
        List<GiftCertificateDTO> giftCertificateDTOS = certificateServiceImpl.findAll();
        return !giftCertificateDTOS.isEmpty() ?
                ResponseEntity.ok().body(giftCertificateDTOS.stream()
                .map(giftCertificateDTO -> linkCreator.toResource(giftCertificateDTO))) :
                ResponseEntity.notFound().build();
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity findOneById(@PathVariable("id")
                                      @Min(value = 0, message = "{violation.id}")
                                      @Max(value = Long.MAX_VALUE, message = "{violation.long.range}")
                                              long id) {
        return ResponseEntity.ok(linkCreator.toResource(certificateServiceImpl.findOne(id)));
//        Optional<GiftCertificateDTO> optionalGiftCertificateDTO = certificateServiceImpl.findById(id);
//        return optionalGiftCertificateDTO.isPresent() ? ResponseEntity.ok().body(optionalGiftCertificateDTO.get())
//                : ResponseEntity.notFound().build();
    }

    @PostMapping
    public ResponseEntity add(@Valid @RequestBody GiftCertificateDTO giftCertificateDTO) {
        GiftCertificateDTO dto = certificateServiceImpl.save(giftCertificateDTO);

        LinkBuilder linkBuilder
                = entityLinks.linkForSingleResource(GiftCertificateDTO.class, dto.getId());
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add(HttpHeaders.LOCATION, linkBuilder.toString());
        return ResponseEntity.status(201).headers(httpHeaders).body(linkCreator.toResource(dto));
    }

    @PutMapping(value = "/{id}")
    public ResponseEntity update(
            @Valid @RequestBody GiftCertificateDTO giftCertificateDTO,
            @PathVariable("id") @Min(value = 0, message = "{violation.id}") long id) {
        return ResponseEntity.ok(certificateServiceImpl.update(giftCertificateDTO, id));
//        Optional<GiftCertificateDTO> optionalGiftCertificateDTO = certificateServiceImpl.update(giftCertificateDTO, id);
//        if (optionalGiftCertificateDTO.isPresent()) {
//            return ResponseEntity.ok(optionalGiftCertificateDTO.get());
//        } else {
//            return ResponseEntity.status(404).body(new ViolationDTO(
//                    List.of(messageSource.getMessage("entity.no", null, null)), 404, LocalDateTime.now()));
//        }
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity delete(@PathVariable("id") @Min(value = 0, message = "{violation.id}") long id) {
        certificateServiceImpl.delete(id);
        return ResponseEntity.status(204).build();
//        return certificateServiceImpl.delete(id) ? ResponseEntity.status(204).build() :
//                ResponseEntity.status(404).body(new ViolationDTO(
//                        List.of(messageSource.getMessage("entity.no", null, null)), 404, LocalDateTime.now()));
    }

    @GetMapping
    public ResponseEntity findByCriteria(@RequestParam Map<String, String> criteriaMap) {
        DataBinder dataBinder = new DataBinder(criteriaMap);
        dataBinder.addValidators(validator);
        dataBinder.validate();
        if (dataBinder.getBindingResult().hasErrors()) {
            ViolationDTO violationDTO = new ViolationDTO();
            List<String> messages = new ArrayList<>();
            dataBinder.getBindingResult().getAllErrors()
                    .forEach(objectError -> messages.add(objectError.getCode()));
            violationDTO.setMessages(messages);
            violationDTO.setStatus(400);
            violationDTO.setLocalDate(LocalDateTime.now());
            return ResponseEntity.badRequest().body(violationDTO);
        }
        SortCriteriaRequestDTO sortCriteriaRequestDTO = dtoParser.parseSortCriteria(criteriaMap);
        SearchCriteriaRequestDTO searchCriteriaRequestDTO = dtoParser.parseSearchCriteria(criteriaMap);
        LimitOffsetCriteriaRequestDTO limitOffsetCriteriaRequestDTO = dtoParser.parseLimitOffsetCriteria(criteriaMap);
        List<GiftCertificateDTO> giftCertificateDTOS = certificateServiceImpl
                .findByCriteria(searchCriteriaRequestDTO, sortCriteriaRequestDTO, limitOffsetCriteriaRequestDTO);
        return ResponseEntity.ok(giftCertificateDTOS);

    }

    @GetMapping(value = "/{id}/tags")
    public ResponseEntity getTagsByCertificate(@PathVariable("id") @Min(value = 0, message = "{violation.id}") long id) {
        return ResponseEntity.ok(tagService.getTagsByCertificate(id));
    }

    @PatchMapping(value = "/{id}")
    public ResponseEntity partialUpdate(@RequestBody @Valid CertificatePatchDTO certificatePatchDTO,
                                        @PathVariable("id") @Min(value = 0, message = "{violation.id}") Long id) {
        return ResponseEntity.ok(certificateServiceImpl.patch(certificatePatchDTO, id));
    }
}
