package com.epam.esm.controller;

import com.epam.esm.constant.EndPointConstant;
import com.epam.esm.constant.RoleConstant;
import com.epam.esm.dto.AppUserPrinciple;
import com.epam.esm.dto.GiftCertificateDTO;
import com.epam.esm.dto.OrderDTO;
import com.epam.esm.dto.OrderSearchCriteriaDTO;
import com.epam.esm.dto.PageAndSortDTO;
import com.epam.esm.dto.PageableList;
import com.epam.esm.dto.TagDTO;
import com.epam.esm.hateoas.CertificateListResource;
import com.epam.esm.hateoas.CertificateResource;
import com.epam.esm.hateoas.OrderListResource;
import com.epam.esm.hateoas.OrderResource;
import com.epam.esm.hateoas.TagListResource;
import com.epam.esm.hateoas.TagResource;
import com.epam.esm.parser.DtoParser;
import com.epam.esm.service.AppUserDetailsService;
import com.epam.esm.service.CertificateService;
import com.epam.esm.service.OrderService;
import com.epam.esm.service.TagService;
import com.epam.esm.validator.CertificateSortValid;
import com.epam.esm.validator.OrderSearchCriteriaValid;
import com.epam.esm.validator.OrderSortValid;
import com.epam.esm.validator.PageAndSizeValid;
import com.epam.esm.validator.TagSortValid;
import lombok.extern.log4j.Log4j2;
import org.springframework.hateoas.ExposesResourceFor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * orders resource endpoints
 *
 * @author Dzmitry Platonov on 2019-10-11.
 * @version 0.0.1
 */
@Log4j2
@RestController
@RequestMapping(EndPointConstant.ORDER_ENDPOINT)
@Validated
@ExposesResourceFor(OrderDTO.class)
public class OrderController {

    private OrderService orderService;
    private DtoParser dtoParser;
    private TagService tagService;
    private CertificateService certificateService;
    private AppUserDetailsService userDetailsService;

    public OrderController(OrderService orderService, DtoParser dtoParser,
                           TagService tagService, CertificateService certificateService,
                           AppUserDetailsService userDetailsService) {
        this.orderService = orderService;
        this.dtoParser = dtoParser;
        this.tagService = tagService;
        this.certificateService = certificateService;
        this.userDetailsService = userDetailsService;
    }

    @Secured({RoleConstant.ROLE_USER, RoleConstant.ROLE_ADMIN})
    @PostMapping
    public ResponseEntity save(@Valid @RequestBody OrderDTO orderDTO) {
        String principleEmail = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        orderDTO.setUserEmail(principleEmail);
        OrderDTO order = orderService.save(orderDTO);
        return ResponseEntity.ok(new OrderResource(order, order.getGiftCertificates().stream()
                .map(giftCertificateDTO -> new CertificateResource(giftCertificateDTO, giftCertificateDTO.getTags().stream()
                        .map(TagResource::new).collect(Collectors.toList()))).collect(Collectors.toList())));
    }

    @PreAuthorize("@securityChecker.check(#params) or hasRole('ROLE_ADMIN')")
    @GetMapping
    public ResponseEntity<OrderListResource> findAll(@PageAndSizeValid(message = "{violation.page.size}")
                                                     @OrderSortValid(message = "{violation.order.sort}")
                                                     @OrderSearchCriteriaValid(message = "{violation.order.search}")
                                                     @RequestParam Map<String, String> params) {
        PageAndSortDTO pageAndSortDTO = dtoParser.parsePageAndSortCriteria(params);
        OrderSearchCriteriaDTO orderSearchCriteriaDTO = dtoParser.parseOrderSearchDTO(params);
        AppUserPrinciple principle = (AppUserPrinciple) userDetailsService
                .loadUserByUsername((String) SecurityContextHolder.getContext().getAuthentication().getPrincipal());
        if (principle.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .anyMatch(s -> s.equals(RoleConstant.ROLE_USER))) {
            orderSearchCriteriaDTO.setEmail(principle.getUsername());
            orderSearchCriteriaDTO.setUserId(null);
        }
        PageableList<OrderDTO> pageableList = orderService.findByCriteria(orderSearchCriteriaDTO, pageAndSortDTO);
        return !pageableList.getList().isEmpty() ? ResponseEntity.ok(
                new OrderListResource(pageableList.getList().stream().map(orderDTO ->
                        new OrderResource(orderDTO, orderDTO.getGiftCertificates().stream()
                                .map(giftCertificateDTO -> new CertificateResource(giftCertificateDTO, giftCertificateDTO.getTags().stream()
                                        .map(TagResource::new).collect(Collectors.toList()))).collect(Collectors.toList()))).collect(Collectors.toList()),
                        pageAndSortDTO.getPage(),
                        pageableList.getLastPage(),
                        pageAndSortDTO.getSize())) :
                ResponseEntity.status(404).build();
    }

    @PostAuthorize("@securityChecker.checkOrder(returnObject) or hasRole('ROLE_ADMIN')")
    @GetMapping("/{id}")
    public ResponseEntity<OrderResource> findOne(@PathVariable @Min(value = 0, message = "{violation.id}") Long id) {
        OrderDTO orderDTO = orderService.findOne(id);
        return ResponseEntity.ok(new OrderResource(orderDTO, orderDTO.getGiftCertificates().stream()
                .map(giftCertificateDTO -> new CertificateResource(giftCertificateDTO, giftCertificateDTO.getTags().stream()
                        .map(TagResource::new).collect(Collectors.toList()))).collect(Collectors.toList())));
    }

    @PreAuthorize("@securityChecker.checkOrderAuthorities(#id) or hasRole('ROLE_ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity delete(@PathVariable @Min(value = 0, message = "{violation.id}") Long id) {
        orderService.delete(id);
        return ResponseEntity.status(204).build();
    }

    @PreAuthorize("@securityChecker.checkOrderAuthorities(#id) or hasRole('ROLE_ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity update(@PathVariable @Min(value = 0, message = "{violation.id}") Long id, @Valid @RequestBody OrderDTO orderDTO) {
        String principleEmail = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        orderDTO.setUserEmail(principleEmail);
        OrderDTO order = orderService.update(orderDTO, id);
        return ResponseEntity.ok(new OrderResource(order, order.getGiftCertificates().stream()
                .map(giftCertificateDTO -> new CertificateResource(giftCertificateDTO, giftCertificateDTO.getTags().stream()
                        .map(TagResource::new).collect(Collectors.toList()))).collect(Collectors.toList())));
    }

    @PreAuthorize("@securityChecker.checkOrderAuthorities(#id) or hasRole('ROLE_ADMIN')")
    @GetMapping(value = "/{id}/tags")
    public ResponseEntity getTagsByOrder(@PathVariable @Min(value = 0, message = "{violation.id}") Long id,
                                         @RequestParam
                                         @TagSortValid(message = "{violation.tag.sort}")
                                         @PageAndSizeValid(message = "{violation.page.size}") Map<String, String> params) {
        PageAndSortDTO pageAndSortDTO = dtoParser.parsePageAndSortCriteria(params);
        PageableList<TagDTO> pageableList = tagService.findTagsByOrder(id, pageAndSortDTO);
        return ResponseEntity.ok(new TagListResource(pageableList.getList().stream()
                .map(TagResource::new).collect(Collectors.toList()), pageAndSortDTO.getPage(),
                pageableList.getLastPage(), pageAndSortDTO.getSize()));
    }

    @PreAuthorize("@securityChecker.checkOrderAuthorities(#id) or hasRole('ROLE_ADMIN')")
    @GetMapping(value = "/{id}/certificates")
    public ResponseEntity getCertificatesByOrder(@PathVariable @Min(value = 0, message = "{violation.id}") Long id,
                                                 @RequestParam
                                                 @CertificateSortValid(message = "{violation.certificate.sort}")
                                                 @PageAndSizeValid(message = "{violation.page.size}") Map<String, String> params) {
        PageAndSortDTO pageAndSortDTO = dtoParser.parsePageAndSortCriteria(params);
        PageableList<GiftCertificateDTO> pageableList = certificateService.findByOrder(id, pageAndSortDTO);

        return !pageableList.getList().isEmpty() ? ResponseEntity.ok(new CertificateListResource(pageableList.getList().stream()
                .map(giftCertificateDTO -> new CertificateResource(giftCertificateDTO, giftCertificateDTO.getTags().stream()
                        .map(TagResource::new).collect(Collectors.toList()))).collect(Collectors.toList()), pageAndSortDTO.getPage(),
                pageableList.getLastPage(), pageAndSortDTO.getSize(), null)) :
                ResponseEntity.status(404).body(pageableList.getList());
    }
}
