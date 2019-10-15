package com.epam.esm.controller;

import com.epam.esm.dto.GiftCertificateDTO;
import com.epam.esm.dto.PageAndSortDTO;
import com.epam.esm.dto.TagDTO;
import com.epam.esm.hateoas.LinkCreator;
import com.epam.esm.parser.DtoParser;
import com.epam.esm.service.CertificateService;
import com.epam.esm.service.TagService;
import com.epam.esm.validator.PageAndSizeValid;
import com.epam.esm.validator.TagSortValid;
import org.springframework.context.MessageSource;
import org.springframework.hateoas.EntityLinks;
import org.springframework.hateoas.ExposesResourceFor;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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
    private LinkCreator linkCreator;
    private DtoParser dtoParser;


    public TagController(TagService tagServiceImpl, CertificateService certificateServiceImpl, EntityLinks entityLinks,
                         SpringValidatorAdapter localValidatorFactoryBean, MessageSource messageSource,
                         LinkCreator linkCreator, DtoParser dtoParser) {
        this.tagServiceImpl = tagServiceImpl;
        this.certificateService = certificateServiceImpl;
        this.entityLinks = entityLinks;
        this.localValidatorFactoryBean = localValidatorFactoryBean;
        this.messageSource = messageSource;
        this.linkCreator = linkCreator;
        this.dtoParser = dtoParser;
    }

    @InitBinder
    protected void initBinder(WebDataBinder binder) {
        binder.setValidator(localValidatorFactoryBean);
    }


    @GetMapping
    public ResponseEntity getAllTags(@PageAndSizeValid @TagSortValid @RequestParam Map<String, String> pageAndSortParameters) {

        List<TagDTO> tagDTOS = tagServiceImpl.findPaginated(dtoParser.parsePageAndSortCriteria(pageAndSortParameters));
        if (!tagDTOS.isEmpty()) {
            List<Resource> resources = new ArrayList<>(tagDTOS.size());
            tagDTOS.forEach(tagDTO -> resources.add(linkCreator.toResource(tagDTO)));
            return ResponseEntity.ok().body(resources);
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity getById(@PathVariable("id")
                                  @Min(value = 0, message = "{violation.id}") long id) {
        return ResponseEntity.ok(linkCreator.toResource(tagServiceImpl.findOne(id)));
    }

    @PostMapping
    public ResponseEntity add(@Valid @RequestBody TagDTO tagDTO) {
        TagDTO saved = tagServiceImpl.save(tagDTO);
        LinkBuilder linkBuilder
                = entityLinks.linkForSingleResource(TagDTO.class, saved.getId());
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add(HttpHeaders.LOCATION, linkBuilder.toString());
        return ResponseEntity.status(201).headers(httpHeaders).body(linkCreator.toResource(saved));
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity delete(@PathVariable("id") @Min(value = 0, message = "{violation.id}") long id) {
        tagServiceImpl.delete(id);
        return ResponseEntity.status(204).build();
    }

    @GetMapping(value = "/{id}/certificates")
    public ResponseEntity getCertificatesByTag(@PathVariable("id") @Min(value = 0, message = "{violation.id}") long id) {
        List<GiftCertificateDTO> giftCertificateDTOS = certificateService.getByTag(id);
        if (!giftCertificateDTOS.isEmpty()) {
            List<Resource> resources = new ArrayList<>(giftCertificateDTOS.size());
            giftCertificateDTOS.forEach(giftCertificateDTO ->
                    resources.add(linkCreator.toResource(giftCertificateDTO)));

            return ResponseEntity.ok(resources);
        }
        return ResponseEntity.ok(certificateService.getByTag(id));
    }

    @GetMapping("/populars")
    public ResponseEntity getMostPopulars(@PageAndSizeValid @RequestParam Map<String, String> requestParams) {
        PageAndSortDTO pageAndSortDTO = dtoParser.parsePageAndSortCriteria(requestParams);
        return ResponseEntity.ok(tagServiceImpl.findPopular(pageAndSortDTO));
    }

}
