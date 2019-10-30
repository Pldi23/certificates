package com.epam.esm.controller;

import com.epam.esm.constant.EndPointConstant;
import com.epam.esm.constant.RoleConstant;
import com.epam.esm.dto.CertificatePatchDTO;
import com.epam.esm.dto.GiftCertificateDTO;
import com.epam.esm.dto.PageAndSortDTO;
import com.epam.esm.dto.SearchCriteriaRequestDTO;
import com.epam.esm.dto.ViolationDTO;
import com.epam.esm.exception.EntityAlreadyExistsException;
import com.epam.esm.hateoas.LinkCreator;
import com.epam.esm.parser.DtoParser;
import com.epam.esm.service.CertificateService;
import com.epam.esm.service.TagService;
import com.epam.esm.util.Translator;
import com.epam.esm.validator.CertificateSortValid;
import com.epam.esm.validator.PageAndSizeValid;
import com.epam.esm.validator.RequestParametersValidator;
import com.epam.esm.validator.TagCostSortValid;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.hateoas.EntityLinks;
import org.springframework.hateoas.ExposesResourceFor;
import org.springframework.hateoas.LinkBuilder;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.validation.DataBinder;
import org.springframework.validation.Validator;
import org.springframework.validation.annotation.Validated;
import org.springframework.validation.beanvalidation.SpringValidatorAdapter;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * certificates resource endpoint
 *
 * @author Dzmitry Platonov on 2019-09-26.
 * @version 0.0.1
 */
@RestController
@RequestMapping(EndPointConstant.CERTIFICATE_ENDPOINT)
@Validated
@ExposesResourceFor(GiftCertificateDTO.class)
public class CertificateController {

    private static final String CERTIFICATE_EXIST_MESSAGE = "exception.certificate.exist";


    private CertificateService certificateServiceImpl;
    private TagService tagService;
    private DtoParser dtoParser;
    private Validator validator;
    private EntityLinks entityLinks;
    private SpringValidatorAdapter localValidatorFactoryBean;
    private LinkCreator linkCreator;


    public CertificateController(CertificateService certificateServiceImpl, DtoParser dtoParser,
                                 RequestParametersValidator validator, TagService tagServiceImpl,
                                 EntityLinks entityLinks,
                                 SpringValidatorAdapter localValidatorFactoryBean,
                                 LinkCreator linkCreator) {
        this.certificateServiceImpl = certificateServiceImpl;
        this.dtoParser = dtoParser;
        this.validator = validator;
        this.tagService = tagServiceImpl;
        this.entityLinks = entityLinks;
        this.localValidatorFactoryBean = localValidatorFactoryBean;
        this.linkCreator = linkCreator;
    }

    @InitBinder
    protected void initBinder(WebDataBinder binder) {
        binder.setValidator(localValidatorFactoryBean);
    }


    @GetMapping(value = "/{id}")
    public ResponseEntity findOneById(@PathVariable("id")
                                      @Min(value = 0, message = "{violation.id}")
                                      @Max(value = Long.MAX_VALUE, message = "{violation.long.range}")
                                              long id) {
        return ResponseEntity.ok(linkCreator.toResource(certificateServiceImpl.findOne(id)));
    }

    @Secured(RoleConstant.ROLE_ADMIN)
    @PostMapping
    public ResponseEntity add(@Valid @RequestBody GiftCertificateDTO giftCertificateDTO) {
        GiftCertificateDTO dto;
        try {
            dto = certificateServiceImpl.save(giftCertificateDTO);
        } catch (DataIntegrityViolationException ex) {
            throw new EntityAlreadyExistsException(String.format(Translator.toLocale(CERTIFICATE_EXIST_MESSAGE),
                    giftCertificateDTO.getName()));
        }

        LinkBuilder linkBuilder
                = entityLinks.linkForSingleResource(GiftCertificateDTO.class, dto.getId());
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add(HttpHeaders.LOCATION, linkBuilder.toString());
        return ResponseEntity.status(201).headers(httpHeaders).body(linkCreator.toResource(dto));
    }

    @Secured(RoleConstant.ROLE_ADMIN)
    @PutMapping(value = "/{id}")
    public ResponseEntity update(
            @Valid @RequestBody GiftCertificateDTO giftCertificateDTO,
            @PathVariable("id") @Min(value = 0, message = "{violation.id}") long id) {
        try {
            return ResponseEntity.ok(linkCreator.toResource(certificateServiceImpl.update(giftCertificateDTO, id)));
        } catch (DataIntegrityViolationException ex) {
            throw new EntityAlreadyExistsException(String.format(Translator.toLocale(CERTIFICATE_EXIST_MESSAGE),
                    giftCertificateDTO.getName()));
        }
    }

    @Secured(RoleConstant.ROLE_ADMIN)
    @DeleteMapping(value = "/{id}")
    public ResponseEntity delete(@PathVariable("id") @Min(value = 0, message = "{violation.id}") long id) {
        certificateServiceImpl.delete(id);
        return ResponseEntity.status(204).build();
    }

    @GetMapping
    public ResponseEntity findByCriteria(@RequestParam
                                         @PageAndSizeValid(message = "{violation.page.size}")
                                         @CertificateSortValid(message = "{violation.certificate.sort}") Map<String, String> criteriaMap) {
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
        PageAndSortDTO pageAndSortDTO = dtoParser.parsePageAndSortCriteria(criteriaMap);
        SearchCriteriaRequestDTO searchCriteriaRequestDTO = dtoParser.parseSearchCriteria(criteriaMap);
        List<GiftCertificateDTO> giftCertificateDTOS = certificateServiceImpl
                .findByCriteria(searchCriteriaRequestDTO, pageAndSortDTO);
        return ResponseEntity.ok(giftCertificateDTOS.stream()
                .map(giftCertificateDTO -> linkCreator.toResource(giftCertificateDTO)));

    }

    @Secured({RoleConstant.ROLE_USER, RoleConstant.ROLE_ADMIN})
    @GetMapping(value = "/{id}/tags")
    public ResponseEntity getTagsByCertificate(
            @PathVariable("id") @Min(value = 0, message = "{violation.id}") long id,
            @PageAndSizeValid(message = "{violation.page.size}")
            @TagCostSortValid(message = "{violation.tag.sort.cost}") @RequestParam Map<String, String> params) {
        PageAndSortDTO pageAndSortDTO = dtoParser.parsePageAndSortCriteria(params);
        return ResponseEntity.ok(tagService.getTagsByCertificate(id, pageAndSortDTO).stream()
                .map(tagDTO -> linkCreator.toResource(tagDTO)));
    }

    @Secured(RoleConstant.ROLE_ADMIN)
    @PatchMapping(value = "/{id}")
    public ResponseEntity partialUpdate(@RequestBody @Valid CertificatePatchDTO certificatePatchDTO,
                                        @PathVariable("id") @Min(value = 0, message = "{violation.id}") Long id) {
        try {
            return ResponseEntity.ok(linkCreator.toResource(certificateServiceImpl.patch(certificatePatchDTO, id)));
        } catch (DataIntegrityViolationException ex) {
            throw new EntityAlreadyExistsException(String.format(Translator.toLocale(CERTIFICATE_EXIST_MESSAGE),
                    certificatePatchDTO.getName()));
        }
    }
}
