package com.epam.esm.controller;

import com.epam.esm.constant.EndPointConstant;
import com.epam.esm.constant.RoleConstant;
import com.epam.esm.dto.GiftCertificateDTO;
import com.epam.esm.dto.PageAndSortDTO;
import com.epam.esm.dto.TagDTO;
import com.epam.esm.hateoas.LinkCreator;
import com.epam.esm.parser.DtoParser;
import com.epam.esm.service.CertificateService;
import com.epam.esm.service.TagService;
import com.epam.esm.validator.CertificateSortValid;
import com.epam.esm.validator.PageAndSizeValid;
import com.epam.esm.validator.TagSortValid;
import org.springframework.hateoas.EntityLinks;
import org.springframework.hateoas.ExposesResourceFor;
import org.springframework.hateoas.LinkBuilder;
import org.springframework.hateoas.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * tags resource endpoint
 *
 * @author Dzmitry Platonov on 2019-09-25.
 * @version 0.0.1
 */
@RestController
@RequestMapping(EndPointConstant.TAG_ENDPOINT)
@Validated
@ExposesResourceFor(TagDTO.class)
public class TagController {

    private TagService tagServiceImpl;
    private CertificateService certificateService;
    private EntityLinks entityLinks;
    private LinkCreator linkCreator;
    private DtoParser dtoParser;


    public TagController(TagService tagServiceImpl, CertificateService certificateServiceImpl, EntityLinks entityLinks,
                         LinkCreator linkCreator, DtoParser dtoParser) {
        this.tagServiceImpl = tagServiceImpl;
        this.certificateService = certificateServiceImpl;
        this.entityLinks = entityLinks;
        this.linkCreator = linkCreator;
        this.dtoParser = dtoParser;
    }


    @Secured({RoleConstant.ROLE_USER, RoleConstant.ROLE_ADMIN})
    @GetMapping
    public ResponseEntity getAllTags(@PageAndSizeValid(message = "{violation.page.size}")
                                     @TagSortValid(message = "{violation.tag.sort.cost}")
                                     @RequestParam Map<String, String> pageAndSortParameters) {

        List<TagDTO> tagDTOS = tagServiceImpl.findPaginated(dtoParser.parsePageAndSortCriteria(pageAndSortParameters));
        if (!tagDTOS.isEmpty()) {
            List<Resource> resources = new ArrayList<>(tagDTOS.size());
            tagDTOS.forEach(tagDTO -> resources.add(linkCreator.toResource(tagDTO)));
            return ResponseEntity.ok().body(resources);
        }
        return ResponseEntity.notFound().build();
    }

    @Secured({RoleConstant.ROLE_USER, RoleConstant.ROLE_ADMIN})
    @GetMapping(value = "/{id}")
    public ResponseEntity getById(@PathVariable("id")
                                  @Min(value = 0, message = "{violation.id}") long id) {
        return ResponseEntity.ok(linkCreator.toResource(tagServiceImpl.findOne(id)));
    }

    @Secured(RoleConstant.ROLE_ADMIN)
    @PostMapping
    public ResponseEntity add(@Valid @RequestBody TagDTO tagDTO) {
        TagDTO saved = tagServiceImpl.save(tagDTO);
        LinkBuilder linkBuilder
                = entityLinks.linkForSingleResource(TagDTO.class, saved.getId());
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add(HttpHeaders.LOCATION, linkBuilder.toString());
        return ResponseEntity.status(201).headers(httpHeaders).body(linkCreator.toResource(saved));
    }

    @Secured(RoleConstant.ROLE_ADMIN)
    @DeleteMapping(value = "/{id}")
    public ResponseEntity delete(@PathVariable("id") @Min(value = 0, message = "{violation.id}") long id) {
        tagServiceImpl.delete(id);
        return ResponseEntity.status(204).build();
    }

    @Secured({RoleConstant.ROLE_USER, RoleConstant.ROLE_ADMIN})
    @GetMapping(value = "/{id}/certificates")
    public ResponseEntity getCertificatesByTag(@PathVariable("id") @Min(value = 0, message = "{violation.id}") long id,
                                               @RequestParam
                                               @PageAndSizeValid(message = "{violation.page.size}")
                                               @CertificateSortValid(message = "{violation.certificate.sort}") Map<String, String> params) {
        PageAndSortDTO pageAndSortDTO = dtoParser.parsePageAndSortCriteria(params);
        List<GiftCertificateDTO> giftCertificateDTOS = certificateService.getByTag(id, pageAndSortDTO);
        if (!giftCertificateDTOS.isEmpty()) {
            List<Resource> resources = new ArrayList<>(giftCertificateDTOS.size());
            giftCertificateDTOS.forEach(giftCertificateDTO ->
                    resources.add(linkCreator.toResource(giftCertificateDTO)));

            return ResponseEntity.ok(resources);
        }
        return ResponseEntity.ok(giftCertificateDTOS);
    }

    @Secured({RoleConstant.ROLE_USER, RoleConstant.ROLE_ADMIN})
    @GetMapping("/populars")
    public ResponseEntity getMostPopulars(@PageAndSizeValid(message = "{violation.page.size}") @RequestParam Map<String, String> requestParams) {
        PageAndSortDTO pageAndSortDTO = dtoParser.parsePageAndSortCriteria(requestParams);
        return ResponseEntity.ok(tagServiceImpl.findPopular(pageAndSortDTO).stream()
                .map(tagDTO -> linkCreator.toResource(tagDTO)));
    }

}
