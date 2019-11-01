package com.epam.esm.controller;

import com.epam.esm.constant.EndPointConstant;
import com.epam.esm.constant.RoleConstant;
import com.epam.esm.dto.GiftCertificateDTO;
import com.epam.esm.dto.PageAndSortDTO;
import com.epam.esm.dto.PageableList;
import com.epam.esm.dto.TagDTO;
import com.epam.esm.dto.TagDetailsDTO;
import com.epam.esm.exception.EntityAlreadyExistsException;
import com.epam.esm.hateoas.CertificateListResource;
import com.epam.esm.hateoas.CertificateResource;
import com.epam.esm.hateoas.TagDetailsListResource;
import com.epam.esm.hateoas.TagDetailsResource;
import com.epam.esm.hateoas.TagListResource;
import com.epam.esm.hateoas.TagResource;
import com.epam.esm.parser.DtoParser;
import com.epam.esm.service.CertificateService;
import com.epam.esm.service.TagService;
import com.epam.esm.util.Translator;
import com.epam.esm.validator.CertificateSortValid;
import com.epam.esm.validator.PageAndSizeValid;
import com.epam.esm.validator.TagCostSortValid;
import com.epam.esm.validator.TagDetailsSortValid;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.hateoas.EntityLinks;
import org.springframework.hateoas.ExposesResourceFor;
import org.springframework.hateoas.LinkBuilder;
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
import java.util.Map;
import java.util.stream.Collectors;

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
    private DtoParser dtoParser;


    public TagController(TagService tagServiceImpl, CertificateService certificateServiceImpl, EntityLinks entityLinks,
                         DtoParser dtoParser) {
        this.tagServiceImpl = tagServiceImpl;
        this.certificateService = certificateServiceImpl;
        this.entityLinks = entityLinks;
        this.dtoParser = dtoParser;
    }


    @Secured({RoleConstant.ROLE_USER, RoleConstant.ROLE_ADMIN})
    @GetMapping
    public ResponseEntity getAllTags(@PageAndSizeValid(message = "{violation.page.size}")
                                     @TagCostSortValid(message = "{violation.tag.sort.cost}")
                                     @RequestParam Map<String, String> pageAndSortParameters) {
        PageAndSortDTO pageAndSortDTO = dtoParser.parsePageAndSortCriteria(pageAndSortParameters);
        PageableList<TagDTO> pageableList = tagServiceImpl.findAllPaginated(pageAndSortDTO);
        if (!pageableList.getList().isEmpty()) {
            return ResponseEntity.ok().body(new TagListResource(pageableList.getList().stream()
                    .map(TagResource::new).collect(Collectors.toList()), pageAndSortDTO.getPage(),
                    pageableList.getLastPage(), pageAndSortDTO.getSize()));
        }
        return ResponseEntity.notFound().build();
    }

    @Secured({RoleConstant.ROLE_USER, RoleConstant.ROLE_ADMIN})
    @GetMapping(value = "/{id}")
    public ResponseEntity getById(@PathVariable("id")
                                  @Min(value = 0, message = "{violation.id}") long id) {
        return ResponseEntity.ok(new TagResource(tagServiceImpl.findOne(id)));
    }

    @Secured(RoleConstant.ROLE_ADMIN)
    @PostMapping
    public ResponseEntity add(@Valid @RequestBody TagDTO tagDTO) {
        TagDTO saved;
        try {
            saved = tagServiceImpl.save(tagDTO);
        } catch (DataIntegrityViolationException ex) {
            throw new EntityAlreadyExistsException(String.format(Translator.toLocale("exception.tag.exist"),
                    tagDTO.getTitle()));
        }
        LinkBuilder linkBuilder
                = entityLinks.linkForSingleResource(TagDTO.class, saved.getId());
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add(HttpHeaders.LOCATION, linkBuilder.toString());
        return ResponseEntity.status(201).headers(httpHeaders).body(new TagResource(saved));
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
        PageableList<GiftCertificateDTO> pageableList = certificateService.getByTag(id, pageAndSortDTO);
        if (!pageableList.getList().isEmpty()) {
            return ResponseEntity.ok(new CertificateListResource(pageableList.getList().stream()
                    .map(CertificateResource::new).collect(Collectors.toList()), pageAndSortDTO.getPage(),
                    pageableList.getLastPage(), pageAndSortDTO.getSize()));
        }
        return ResponseEntity.ok(pageableList.getList());
    }

    @Secured({RoleConstant.ROLE_ADMIN})
    @GetMapping("/populars")
    public ResponseEntity getMostPopulars(@PageAndSizeValid(message = "{violation.page.size}") @RequestParam Map<String, String> requestParams) {
        PageAndSortDTO pageAndSortDTO = dtoParser.parsePageAndSortCriteria(requestParams);
        PageableList<TagDTO> pageableList = tagServiceImpl.findPopular(pageAndSortDTO);
        return ResponseEntity.ok(new TagListResource(pageableList.getList().stream()
                .map(TagResource::new).collect(Collectors.toList()), pageAndSortDTO.getPage(),
                pageableList.getLastPage(), pageAndSortDTO.getSize()));
    }

    @Secured({RoleConstant.ROLE_ADMIN})
    @GetMapping("/payable")
    public ResponseEntity getTagsWithDetails(@PageAndSizeValid(message = "{violation.page.size}")
                                             @TagDetailsSortValid(message = "{violation.sort.tag.details}")
                                             @RequestParam Map<String, String> requestParams) {
        PageAndSortDTO pageAndSortDTO = dtoParser.parsePageAndSortCriteria(requestParams);
        PageableList<TagDetailsDTO> pageableList = tagServiceImpl.findTagsWithDetails(pageAndSortDTO);
        return ResponseEntity.ok(new TagDetailsListResource(pageableList.getList().stream()
                .map(TagDetailsResource::new).collect(Collectors.toList())));
    }

    @Secured({RoleConstant.ROLE_ADMIN})
    @GetMapping("{id}/stats")
    public ResponseEntity getOneTagWithDetails(@PathVariable("id")
                                               @Min(value = 0, message = "{violation.id}") long id) {
        return ResponseEntity.ok(new TagDetailsResource(tagServiceImpl.findTagDetails(id)));
    }

    @Secured(RoleConstant.ROLE_ADMIN)
    @GetMapping("/{id}/cost")
    public ResponseEntity getTagCost(@PathVariable("id") @Min(value = 0, message = "{violation.id}") long id) {
        return ResponseEntity.ok(tagServiceImpl.calculateTagCost(id));
    }

}
