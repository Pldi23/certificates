package com.epam.esm.controller;

import com.epam.esm.dto.OrderDTO;
import com.epam.esm.dto.OrderSearchCriteriaDTO;
import com.epam.esm.dto.PageAndSortDTO;
import com.epam.esm.hateoas.LinkCreator;
import com.epam.esm.parser.DtoParser;
import com.epam.esm.service.OrderService;
import com.epam.esm.service.TagService;
import com.epam.esm.validator.OrderSearchCriteriaValid;
import com.epam.esm.validator.PageAndSizeValid;
import com.epam.esm.validator.TagSortValid;
import org.springframework.hateoas.ExposesResourceFor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
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
import java.util.List;
import java.util.Map;

/**
 * gift-certificates
 *
 * @author Dzmitry Platonov on 2019-10-11.
 * @version 0.0.1
 */
@RestController
@RequestMapping("/orders")
@Validated
@ExposesResourceFor(OrderDTO.class)
public class OrderController {

    private OrderService orderService;
    private LinkCreator linkCreator;
    private DtoParser dtoParser;
    private TagService tagService;

    public OrderController(OrderService orderService, LinkCreator linkCreator, DtoParser dtoParser, TagService tagService) {
        this.orderService = orderService;
        this.linkCreator = linkCreator;
        this.dtoParser = dtoParser;
        this.tagService = tagService;
    }

    @Secured({"ROLE_USER", "ROLE_ADMIN"})
    @PostMapping
    public ResponseEntity save(@Valid @RequestBody OrderDTO orderDTO) {
        return ResponseEntity.ok(linkCreator.toResource(orderService.save(orderDTO)));
    }

    @Secured("ROLE_ADMIN")
    @GetMapping
    public ResponseEntity findAll(@PageAndSizeValid @OrderSearchCriteriaValid @RequestParam Map<String,String> params) {
        PageAndSortDTO pageAndSortDTO = dtoParser.parsePageAndSortCriteria(params);
        OrderSearchCriteriaDTO orderSearchCriteriaDTO = dtoParser.parseOrderSearchDTO(params);
        List<OrderDTO> dtos = orderService.findByCriteria(orderSearchCriteriaDTO, pageAndSortDTO);
        return !dtos.isEmpty() ? ResponseEntity.ok(dtos.stream().map(orderDTO ->
                linkCreator.toResource(orderDTO))) : ResponseEntity.status(404).body(dtos);
    }

    @Secured("ROLE_ADMIN")
    @GetMapping("/{id}")
    public ResponseEntity findOne(@PathVariable @Min(0) Long id) {
        return ResponseEntity.ok(linkCreator.toResource(orderService.findOne(id)));
    }

    @Secured("ROLE_ADMIN")
    @DeleteMapping("/{id}")
    public ResponseEntity delete(@PathVariable @Min(0) Long id) {
        orderService.delete(id);
        return ResponseEntity.status(204).build();
    }

    @Secured("ROLE_ADMIN")
    @PutMapping("/{id}")
    public ResponseEntity update(@PathVariable @Min(0) Long id, @Valid @RequestBody OrderDTO orderDTO) {
        return ResponseEntity.ok(linkCreator.toResource(orderService.update(orderDTO, id)));
    }

    @Secured({"ROLE_USER", "ROLE_ADMIN"})
    @GetMapping(value = "/{id}/tags")
    public ResponseEntity getTagsByOrder(@PathVariable @Min(0) Long id,
                                         @RequestParam @TagSortValid @PageAndSizeValid Map<String, String> params) {
        PageAndSortDTO pageAndSortDTO = dtoParser.parsePageAndSortCriteria(params);
        return ResponseEntity.ok(tagService.findTagsByOrder(id, pageAndSortDTO));
    }
}
